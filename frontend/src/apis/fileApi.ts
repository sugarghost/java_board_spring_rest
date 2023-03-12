import { ref, Ref } from "vue";

import axios from "../axios/axios";
import type { IFile } from "../types/file";

export function getFileListApi() {
  const getFileList = async (articleId: number) => {
    let fileList = <IFile[]>[];
    try {
      const response = await axios.get(`/articles/${articleId}/files`, {});
      fileList = {
        ...response.data,
      };
    } catch (error) {
      console.error(error);
    }
    return fileList;
  };

  return {
    getFileList,
  };
}
export function insertCommentApi() {
  const insertComment = async (articleId: number, content: string) => {
    let result;
    try {
      const response = await axios.post(`/articles/${articleId}/comments`, {
        articleId,
        content,
      });
      result = response.status;
    } catch (error) {
      console.error(error);
    }
    return result;
  };

  return {
    insertComment,
  };
}

export function downloadFileApi() {
  const getFileList = async (fileId: bigint, articleId: bigint, fileOriginName: string) => {
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

  return {
    getFileList,
  };
}
