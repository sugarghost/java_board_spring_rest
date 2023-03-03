<template lang="">
    <v-container>
    <h1 class="mb-4">자유게시판 - 상세</h1>
            <v-row>
      <v-col class="m-2 mr-auto">{{ article.writer}}</v-col>
      <v-col class="m-2">
        <span>{{ formatDateSecond(article.createdDate) }}</span>
      </v-col>
      <v-col class="m-2">
        <span>
          {{ article.modifiedDate ? formatDateSecond(articleDTO.modifiedDate) : '-' }}
        </span>
      </v-col>
    </v-row>
    <v-row>
      <v-col class="m-2">{{ `[${article.categoryName}]` }}</v-col>
      <v-col class="m-2 mr-auto text-break">{{ article.title }}</v-col>
      <v-col class="m-2">
        조회수: {{ article.viewCount }}
      </v-col>
    </v-row>
    <hr>
    <v-row class="my-3">
      <v-col class="w-100 border text-break">{{ article.content }}</v-col>
    </v-row>
    <v-row>
      <v-col class="w-100">
        <template v-if="article.isFileExist">
          <template v-for="fileDTO in fileList" :key="fileDTO.fileId">
            <a href="javascript:void(0);" @click="fileDownload(fileDTO.fileId)">
              {{ fileDTO.fileOriginName }}
            </a>
            <br>
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
                <v-textarea v-model="commentContent" label="댓글 내용" :rules="commentContentRules"></v-textarea>

            </v-col>
            <v-col cols="2">
              <v-btn color="primary" @click="validateCommentForm">
                댓글 등록
              </v-btn>
            </v-col>
          </v-row>
        </v-form>
      </v-col>
    </v-row>
    </v-container>
</template>
<script>
import { ref, computed, onBeforeMount, inject } from "vue";
import { useStore } from "vuex";
import { useRouter, useRoute } from "vue-router"
import { formatDateSecond } from "../assets/common";

export default {
    name: "ArticleDetail",
    setup() {
        // 공통 요소
        const axios = inject("axios");
        const route = useRoute()

        // article Data 가져오기
        const { articleId } = route.params
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

        const getArticle = async () => {
            try {
                const response = await axios.get(`/v1/articles/${articleId}`, {});
                article.value = response.data;

            } catch (error) {
                console.error(error);
            }
        }
        onBeforeMount(() => {
            getArticle();
        });


        // comment Data 가져오기
        const commentList = ref([]);
        const getCommentList = async () => {
            try {
                const response = await axios.get(`/v1/articles/${articleId}/comments`, {});
                commentList.value = {
                    ...response.data,
                }

            } catch (error) {
                console.error(error);
            }
        }
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

        // 등록시 validation 체크 후 등록
        const validateCommentForm = async () => {
            if (!commentFormValid.value) {
                return;
            }
            try {
                const response = await axios.post(`/v1/articles/${articleId}/comments`, {
                    content: commentContent.value,
                });
                // 성공한 경우에만 댓글 목록 갱신
                if (response.status === 201 || response.status === 200) {
                    getCommentList();
                }
            } catch (error) {
                console.error(error);
            }
        }


        // file Data 가져오기
        const fileList = ref([]);
        const getFileList = async () => {
            try {
                const response = await axios.get(`/v1/articles/${articleId}/files`, {});
                fileList.value = response.data;

            } catch (error) {
                console.error(error);
            }
        }
        onBeforeMount(() => {
            getFileList();
        });

        // file download
        const fileDownload = async (fileId) => {
            try {
                const response = await axios.get(`/v1/articles/${articleId}/files/${fileId}`, {
                    responseType: "blob",
                    headers: {
                        "Accept": "application/octet-stream"
                    }
                });

                const url = window.URL.createObjectURL(new Blob([response.data]));
                const link = document.createElement("a");
                link.href = url;
                link.setAttribute("download", response.headers["content-disposition"].split("filename=")[1]);
                document.body.appendChild(link);
                link.click();
            } catch (error) {
                console.error(error);
            }
        }

        return {
            article,
            commentList,
            commentFormValid,
            commentContent,
            commentContentRules,
            validateCommentForm,
            formatDateSecond,
            fileList,
            fileDownload,
        }
    }

}
</script>
<style lang="">
    
</style>