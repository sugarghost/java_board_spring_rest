<template lang="">
  <v-container>
    <h1 class="mb-4">자유게시판 - 상세</h1>
    <v-row>
      <v-col class="m-2 mr-auto">{{ article.writer }}</v-col>
      <v-col class="m-2">
        <span>{{ formatDateSecond(article.createdDate) }}</span>
      </v-col>
      <v-col class="m-2">
        <span>
          {{ formatDateSecond(article.modifiedDate) }}
        </span>
      </v-col>
    </v-row>
    <v-row>
      <v-col class="m-2">{{ `[${article.categoryName}]` }}</v-col>
      <v-col class="m-2 mr-auto text-break">{{ article.title }}</v-col>
      <v-col class="m-2"> 조회수: {{ article.viewCount }} </v-col>
    </v-row>
    <hr />
    <v-row class="my-3">
      <v-col class="w-100 border text-break">{{ article.content }}</v-col>
    </v-row>
    <v-row>
      <v-col class="w-100">
        <template v-if="article.isFileExist">
          <template v-for="fileDTO in fileList" :key="fileDTO.fileId">
            <a
              href="javascript:void(0);"
              @click="fileDownload(fileDTO.fileId, fileDTO.fileOriginName)"
            >
              {{ fileDTO.fileOriginName }}
            </a>
            <br />
          </template>
        </template>
      </v-col>
    </v-row>
    <v-row class="bg-light">
      <v-col>
        <div id="commentList">
          <template v-if="commentList">
            <template v-for="commentDTO in commentList" :key="commentDTO.commentId">
              <div class="row justify-content-start px-2 pt-2">
                {{ formatDateSecond(commentDTO.createdDate) }}
              </div>
              <div class="row border-bottom px-2 pb-3 text-break">
                {{ commentDTO.content }}
              </div>
            </template>
          </template>
        </div>
        <v-form v-model="commentFormValid" @submit.prevent="validateCommentForm">
          <v-row class="py-3">
            <v-col cols="10">
              <v-textarea
                v-model="commentContent"
                label="댓글 내용"
                :rules="commentContentRules"
              ></v-textarea>
            </v-col>
            <v-col cols="2">
              <v-btn color="primary" @click="validateCommentForm"> 댓글 등록 </v-btn>
            </v-col>
          </v-row>
        </v-form>
      </v-col>
    </v-row>
    <v-row>
      <v-col class="text-center">
        <v-btn color="primary" @click="$router.push('/list')">목록</v-btn>
        <v-btn color="" @click="$router.push(`/modify/${articleId}`)">수정</v-btn>
        <v-btn color="" @click="isDeleteModalShow = true">삭제</v-btn>
      </v-col>
    </v-row>
    <v-dialog v-model="isDeleteModalShow" width="1024" persistent>
      <v-card>
        <v-form v-model="deleteFormValid" @submit.prevent="deleteArticle">
          <v-card-title>
            <span class="text-h5">게시물 삭제</span>
          </v-card-title>
          <v-card-text>
            <v-text-field
              label="Password*"
              type="password"
              v-model="password"
              :rules="passwordRules"
              required
            ></v-text-field>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="blue-darken-1" variant="text" @click="isDeleteModalShow = false">
              취소
            </v-btn>
            <v-btn color="blue-darken-1" variant="text" @click="deleteArticle"> 삭제 </v-btn>
          </v-card-actions>
        </v-form>
      </v-card>
    </v-dialog>
  </v-container>
</template>
<script>
import { ref, computed, onBeforeMount, inject } from "vue";
import { useStore } from "vuex";

import { useRouter, useRoute } from "vue-router";
import axios from "../axios/axios";
import { formatDateSecond } from "../assets/common";

import { getArticleApi } from "../apis/getArticlesApi";

export default {
  name: "ArticleDetail",
  setup() {
    // 공통 요소
    const route = useRoute();
    const router = useRouter();

    // article Data 구성요소
    const { articleId } = route.params;

    const { article, getArticle } = getArticleApi();

    onBeforeMount(() => {
      getArticle(articleId);
    });

    // comment Data 가져오기
    const commentList = ref([]);
    const getCommentList = async () => {
      try {
        const response = await axios.get(`/articles/${articleId}/comments`, {});
        commentList.value = {
          ...response.data,
        };
      } catch (error) {
        console.error(error);
      }
    };
    onBeforeMount(() => {
      getCommentList();
    });

    // comment 등록
    const commentFormValid = ref(false);
    const commentContent = ref("");
    const commentContentRules = [
      (value) => !!value || "내용을 입력해주세요",
      (value) => /^.{1,255}$/g.test(value) || "내용은 1~255자로 입력해주세요",
    ];

    // comment 등록시 validation 체크 후 등록
    const validateCommentForm = async () => {
      if (!commentFormValid.value) {
        return;
      }
      try {
        const response = await axios.post(`/articles/${articleId}/comments`, {
          articleId,
          content: commentContent.value,
        });
        // 성공한 경우에만 댓글 목록 갱신
        if (response.status === 201 || response.status === 200) {
          getCommentList();
        }
      } catch (error) {
        console.error(error);
      }
    };

    // file Data 가져오기
    const fileList = ref([]);
    const getFileList = async () => {
      try {
        const response = await axios.get(`/articles/${articleId}/files`, {});
        fileList.value = response.data;
      } catch (error) {
        console.error(error);
      }
    };
    onBeforeMount(() => {
      getFileList();
    });

    // file download
    const fileDownload = async (fileId, fileOriginName) => {
      try {
        // TODO: spring Server에 Range 구현은 했지만 사용은 보류(테스트도 안해봄)
        const response = await axios.get(`/articles/${articleId}/files/${fileId}`, {
          responseType: "blob",
          headers: {
            Accept: "application/octet-stream",
          },
        });
        console.log(response);
        const url = window.URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement("a");
        link.href = url;
        link.setAttribute("download", fileOriginName);
        document.body.appendChild(link);
        link.click();
      } catch (error) {
        console.error(error);
      }
    };

    // delete modal 설정
    const isDeleteModalShow = ref(false);

    // delete 관련 요소 설정
    const deleteFormValid = ref(false);
    const password = ref(null);
    const passwordRules = [
      (value) => !!value || "비밀번호를 입력해주세요",
      (value) =>
        /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{4,15}$/.test(value) ||
        "비밀번호는 영문 대소문자, 숫자, 특수문자(!@#$%^&*)를 조합한 4~15자로 입력해주세요",
    ];

    // article delete
    const deleteArticle = async () => {
      if (!deleteFormValid.value) {
        return;
      }
      try {
        const response = await axios.delete(`/articles/${articleId}`, {
          data: {
            articleId,
            password: password.value,
          },
        });
        if (response.status === 200 || response.status === 204) {
          router.push("/list");
        }
      } catch (error) {
        // TODO: 에러처리 좀더 우아하게 고민하기
        if (error.response.data.code === "ARTICLE.ERR.400.DTO.PASSWORD") {
          alert("비밀번호가 일치하지 않습니다.");
        }
        console.error(error);
      }
    };

    return {
      article,
      articleId,
      commentList,
      commentFormValid,
      commentContent,
      commentContentRules,
      validateCommentForm,
      formatDateSecond,
      fileList,
      fileDownload,
      isDeleteModalShow,
      deleteFormValid,
      password,
      passwordRules,
      deleteArticle,
    };
  },
};
</script>
<style lang=""></style>
