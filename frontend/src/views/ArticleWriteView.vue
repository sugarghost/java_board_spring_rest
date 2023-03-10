<template>
  <v-container fluid>
    <h1>자유게시판 - 등록</h1>
    <v-form v-model="formValid" @submit.prevent="submitForm">

      <v-card>
        <v-card-title>카테고리 *</v-card-title>
        <v-card-text>
          <v-select v-model="categoryId" :items="categories" item-title="name" item-value="categoryId" single-line
            :rules="categoryIdRules" label="카테고리"></v-select>

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
          <v-text-field v-model="password" label="비밀번호" type="password" :rules="passwordRules"></v-text-field>
          <v-text-field v-model="passwordCheck" label="비밀번호 확인" type="password"
            :rules="passwordCheckRules"></v-text-field>
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
        <v-card-title>파일첨부</v-card-title>
        <v-card-text>
          <v-file-input multiple v-model="files" label="파일 찾기" id="customFileInput" name="file1"></v-file-input>
        </v-card-text>
      </v-card>

      <div class="d-flex justify-space-between">
        <v-btn class="float-left" @click="$router.push('/list')">취소</v-btn>
        <v-btn color="primary" class="float-right" @click="submitForm">저장</v-btn>
      </div>

    </v-form>
  </v-container>
</template>

<script>
import { ref, onBeforeMount, inject } from "vue";
import { useStore } from "vuex";
import { useRouter, useRoute } from "vue-router"

export default {
  name: "ArticleWrite",

  setup() {
    const axios = inject("axios");
    const store = useStore();
    const router = useRouter();


    // 카테고리 목록 조회
    // TODO: List랑 중복되고 하는데, 공통 모듈로 따로 빼는 방안 고려 필요
    //
    const categories = ref([]);
    const getCategories = async () => {
      try {
        const response = await axios.get("/v1/categories", {});

        categories.value = response.data.map((categoryData) => ({
          categoryId: categoryData.categoryId,
          name: categoryData.name,
        }))
      } catch (error) {
        console.error(error);
      }
    };
    onBeforeMount(() => {
      getCategories();
    });


    // form에 매핑될 ref들과 해당 ref들에 validation을 위한 rules
    // TODO: 생각해보니 객체 하나로 묶어서 관리하는게 더 좋을 것 같다.
    // TODO: 반복되는 요소니 나중에 공통으로 빼기
    const formValid = ref(false);

    const categoryId = ref(null);
    const categoryIdRules = [(value) => !!value || "카테고리를 선택해주세요"];

    const writer = ref(null);
    const writerRules = [
      (value) => !!value || "작성자를 입력해주세요",
      (value) => /^.{3,4}$/g.test(value) || "작성자는 3~4자로 입력해주세요",];
    const password = ref(null);
    const passwordRules = [
      (value) => !!value || "비밀번호를 입력해주세요",
      (value) =>
        /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{4,15}$/.test(
          value
        ) || "비밀번호는 영문 대소문자, 숫자, 특수문자(!@#$%^&*)를 조합한 4~15자로 입력해주세요",];
    const passwordCheck = ref(null);
    const passwordCheckRules = [
      (value) => !!value || "비밀번호 확인을 입력해주세요",
      (v) => v === password.value || "비밀번호가 일치하지 않습니다",];
    const title = ref(null);
    const titleRules = [
      (value) => !!value || "제목을 입력해주세요",
      (value) => /^.{4,99}$/g.test(value) || "제목은 4~99자로 입력해주세요",];
    const content = ref(null);
    const contentRules = [
      (value) => !!value || "내용을 입력해주세요",
      (value) => /^.{4,1999}$/g.test(value) || "내용은 4~1999자로 입력해주세요",];
    const files = ref([]);

    // form submit 처리
    const submitForm = () => {
      // form validation 체크
      if (formValid.value) {
        const formData = new FormData();
        formData.append("articleDTO",
          new Blob([JSON.stringify({
            categoryId: categoryId.value,
            writer: writer.value,
            password: password.value,
            title: title.value,
            content: content.value,
          })], { type: "application/json" })
        );
        // TODO: 비랍으로, 베이스로 다양한 방법으로 넘기는 방법이있음
        for (let i = 0; i < files.value.length; i += 1) {
          formData.append("files", files.value[i]);
        }
        // TODO: axios는 별도로 빼기 + 인터셉터 적용(고려)
        axios.post("/v1/articles", formData, {
          headers: {
            "Content-Type": "multipart/form-data"
          }
        }).then(response => {
          // 성공 했으면 list로 귀환
          if (response.status === 201 || response.status === 200) {
            // 가기전에 검색조건 초기화해서 방금 insert한 데이터가 보이도록
            store.commit("updateSearchParams", {
              categoryId: null,
              startDate: null,
              endDate: null,
              searchWord: null,
              articlePerPage: 10,
              pageNum: 1,
            });
            router.push("/list")
          }

        }).catch(error => {
          console.error(error);
        });
      } else {
        // TODO: form validation 실패 시 처리(alert말고 다른 방법으로)
        // Rules 가져다 쓰니깐 어떤 요소실패했는지 focus가 안가고있음
        alert("form validation 실패");
      }
    };




    return {
      categories,
      getCategories,
      formValid,
      categoryId,
      categoryIdRules,
      writer,
      writerRules,
      password,
      passwordRules,
      passwordCheck,
      passwordCheckRules,
      title,
      titleRules,
      content,
      contentRules,
      files,
      submitForm,
    };
  }

};
</script>
