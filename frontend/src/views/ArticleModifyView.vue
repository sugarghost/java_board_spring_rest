<template lang="">
  <v-container fluid>
    <h1>자유게시판 - 수정</h1>
    <v-form v-model="formValid" @submit.prevent="submitForm">
      <v-card>
        <v-card-title>카테고리</v-card-title>
        <v-card-text>
          {{ article.categoryName }}
        </v-card-text>
      </v-card>
      <v-card>
        <v-card-title>등록 일시</v-card-title>
        <v-card-text>
          {{ formatDateSecond(article.createdDate) }}
        </v-card-text>
      </v-card>

      <v-card>
        <v-card-title>수정 일시</v-card-title>
        <v-card-text>
          {{ formatDateSecond(article.modifiedDate) }}
        </v-card-text>
      </v-card>
      <v-card>
        <v-card-title>조회수</v-card-title>
        <v-card-text>
          {{ article.viewCount }}
        </v-card-text>
      </v-card>

      <v-card>
        <v-card-title>작성자 *</v-card-title>
        <v-card-text>
          <v-text-field v-model="writer" label="작성자" :rules="writerRules"></v-text-field>
        </v-card-text>
      </v-card>

      <v-card>
        <v-card-title>비밀번호 *</v-card-title>
        <v-card-text>
          <v-text-field
            v-model="password"
            label="비밀번호"
            type="password"
            :rules="passwordRules"
          ></v-text-field>
        </v-card-text>
      </v-card>

      <v-card>
        <v-card-title>제목 *</v-card-title>
        <v-card-text>
          <v-text-field v-model="title" label="제목" :rules="titleRules"></v-text-field>
        </v-card-text>
      </v-card>

      <v-card>
        <v-card-title>내용 *</v-card-title>
        <v-card-text>
          <v-textarea v-model="content" label="내용" :rules="contentRules"></v-textarea>
        </v-card-text>
      </v-card>

      <v-card>
        <v-card-title>파일 리스트</v-card-title>
        <v-card-text>
          <template v-if="article.isFileExist">
            <v-row v-for="fileDTO in fileList" :key="fileDTO.fileId">
              {{ fileDTO.fileOriginName }}
              <v-btn @click="fileDownload(fileDTO.fileId)">Download</v-btn>
              <v-btn @click="fileDelete(fileDTO.fileId)">X</v-btn>
            </v-row>
          </template>
        </v-card-text>
      </v-card>

      <v-card>
        <v-card-title>파일첨부</v-card-title>
        <v-card-text>
          <v-file-input
            multiple
            v-model="files"
            label="파일 찾기"
            id="customFileInput"
            name="file1"
          ></v-file-input>
        </v-card-text>
      </v-card>

      <div class="d-flex justify-space-between">
        <v-btn class="float-left" @click="$router.push(`/view/${articleId}`)">취소</v-btn>
        <v-btn color="primary" class="float-right" @click="submitForm">저장</v-btn>
      </div>
    </v-form>
  </v-container>
</template>
<script>
import { ref, onBeforeMount, inject } from "vue";
import { useStore } from "vuex";
import { useRouter, useRoute } from "vue-router";
import axios from "../axios/axios";
import { formatDateSecond } from "../assets/common";

export default {
  name: "ArticleModify",

  setup() {
    const store = useStore();
    const router = useRouter();
    const route = useRoute();

    // article Data 구성요소
    const { articleId } = route.params;
    const article = ref({
      writer: "",
      title: "",
      content: "",
      categoryName: "",
      viewCount: 0,
      isFileExist: false,
      createdDate: "",
      modifiedDate: "",
    });

    // article Data 가져오기
    const getArticle = async () => {
      try {
        const response = await axios.get(`/articles/${articleId}`, {});
        article.value = response.data;
      } catch (error) {
        console.error(error);
      }
    };
    onBeforeMount(() => {
      getArticle();
    });

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
    const fileDownload = async (fileId) => {
      try {
        // TODO: spring Server에 Range 구현은 했지만 사용은 보류(테스트도 안해봄)
        const response = await axios.get(`/articles/${articleId}/files/${fileId}`, {
          responseType: "blob",
          headers: {
            Accept: "application/octet-stream",
          },
        });

        const url = window.URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement("a");
        link.href = url;
        link.setAttribute(
          "download",
          response.headers["content-disposition"].split("filename=")[1]
        );
        document.body.appendChild(link);
        link.click();
      } catch (error) {
        console.error(error);
      }
    };

    // file delete
    const deleteFiles = ref([]);
    const fileDelete = (fileId) => {
      deleteFiles.value.push(fileId);
      fileList.value = fileList.value.filter((file) => file.fileId !== fileId);
    };

    // form 관련 요소

    const formValid = ref(false);

    const writer = ref(null);
    const writerRules = [
      (value) => !!value || "작성자를 입력해주세요",
      (value) => /^.{3,4}$/g.test(value) || "작성자는 3~4자로 입력해주세요",
    ];
    const password = ref(null);
    const passwordRules = [
      (value) => !!value || "비밀번호를 입력해주세요",
      (value) =>
        /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{4,15}$/.test(value) ||
        "비밀번호는 영문 대소문자, 숫자, 특수문자(!@#$%^&*)를 조합한 4~15자로 입력해주세요",
    ];
    const title = ref(null);
    const titleRules = [
      (value) => !!value || "제목을 입력해주세요",
      (value) => /^.{4,99}$/g.test(value) || "제목은 4~99자로 입력해주세요",
    ];
    const content = ref(null);
    const contentRules = [
      (value) => !!value || "내용을 입력해주세요",
      (value) => /^.{4,1999}$/g.test(value) || "내용은 4~1999자로 입력해주세요",
    ];
    const files = ref([]);

    // form submit 처리

    const submitForm = () => {
      // form validation 체크
      if (formValid.value) {
        const formData = new FormData();
        formData.append(
          "articleDTO",
          new Blob(
            [
              JSON.stringify({
                articleId,
                writer: writer.value,
                password: password.value,
                title: title.value,
                content: content.value,
              }),
            ],
            { type: "application/json" }
          )
        );
        for (let i = 0; i < deleteFiles.value.length; i += 1) {
          formData.append("deleteFiles", deleteFiles.value[i]);
        }

        for (let i = 0; i < files.value.length; i += 1) {
          formData.append("files", files.value[i]);
        }

        axios
          .put(`/articles/${articleId}`, formData, {
            headers: {
              "Content-Type": "multipart/form-data",
            },
          })
          .then((response) => {
            // 성공 했으면 view로 귀환
            if (response.status === 204 || response.status === 200) {
              router.push(`/view/${articleId}`);
            }
          })
          .catch((error) => {
            console.error(error);
          });
      } else {
        alert("form validation 실패");
      }
    };

    return {
      articleId,
      article,
      formValid,
      writer,
      writerRules,
      password,
      passwordRules,
      title,
      fileList,
      titleRules,
      content,
      contentRules,
      files,
      submitForm,
      formatDateSecond,
      fileDownload,
      fileDelete,
      deleteFiles,
    };
  },
};
</script>
<style lang=""></style>
