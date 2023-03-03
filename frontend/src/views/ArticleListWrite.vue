<template>

<div class="container">
  <h1 th:text="#{thymeleaf.write.title}">자유게시판 - 등록</h1>
  <form id="articleForm" name="articleForm" method="post"
        enctype="multipart/form-data">

    <table class="table">
      <tbody>
      <tr>
        <td class="bg-light" th:text="#{thymeleaf.write.form.category}">카테고리 *</td>
        <td>
          <select class="form-control" name="categoryId">
            <option value="" th:text="#{thymeleaf.write.form.category.category}">카테고리</option>
            <th:block th:each="categoryDTO : ${categoryList}">
              <option th:value="${categoryDTO.categoryId}"
                      th:text="${categoryDTO.name}"></option>
            </th:block>
          </select>
        </td>
      </tr>
      <tr>
        <td class="bg-light" th:text="#{thymeleaf.write.form.writer}">작성자 *</td>
        <td><input type="text" class="form-control" name="writer"/></td>
      </tr>

      <tr>
        <td class="bg-light" th:text="#{thymeleaf.write.form.password}">비밀번호 *</td>
        <td>
          <input type="password" class="form-control" name="password" th:placeholder="#{thymeleaf.write.form.password.placeholder}"/>
          <input type="password" class="form-control" name="passwordCheck"
                 th:placeholder="#{thymeleaf.write.form.password.confirm.placeholder}">
        </td>
      </tr>
      <tr>
        <td class="bg-light" th:text="#{thymeleaf.write.form.title}">제목 *</td>
        <td><input type="text" class="form-control" name="title"/></td>
      </tr>
      <tr>
        <td class="bg-light" th:text="#{thymeleaf.write.form.content}">내용 *</td>
        <td><textarea class="form-control" name="content"></textarea></td>
      </tr>
      <tr>
        <td class="bg-light" th:text="#{thymeleaf.write.form.file}">파일첨부</td>
        <td>
          <div class="input-group">
            <div class="custom-file">
              <input type="file" class="custom-file-input" id="customFileInput1"
                     name="file1">
              <label class="custom-file-label" for="customFileInput1"
                     aria-describedby="inputGroupFile1" th:text="#{thymeleaf.write.form.file.search}">파일 찾기</label>
            </div>
          </div>

          <div class="input-group">
            <div class="custom-file">
              <input type="file" class="custom-file-input" id="customFileInput2"
                     name="file2">
              <label class="custom-file-label" for="customFileInput2"
                     aria-describedby="inputGroupFile2" th:text="#{thymeleaf.write.form.file.search}">파일 찾기</label>
            </div>
          </div>

          <div class="input-group">
            <div class="custom-file">
              <input type="file" class="custom-file-input" id="customFileInput3"
                     name="file3">
              <label class="custom-file-label" for="customFileInput3"
                     aria-describedby="inputGroupFile3" th:text="#{thymeleaf.write.form.file.search}">파일 찾기</label>
            </div>
          </div>
        </td>
      </tr>
      </tbody>
    </table>
    <div>
      <button type="button" class="btn border float-left"
      @click="$router.push('/')">취소
      </button>
    </div>
    <div>
      <button type="button" class="btn btn-primary float-right" @click="$router.push('/write')">저장</button>
    </div>
  </form>
</div>
  </template>
  
  <script>
  import { ref, computed, onMounted, inject } from "vue";
  import {useStore} from "vuex";
  
  export default {
    name: "ArticleWrite",
  
    setup() {
      const axios = inject("axios");
      const store = useStore();
  
  
      return {
      };
    }
  
  };
  </script>