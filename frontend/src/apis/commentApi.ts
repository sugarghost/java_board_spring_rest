import { ref, Ref } from "vue";

import axios from "../axios/axios";
import type { IComment } from "../types/comment";

export function getCommentListApi() {
  const getCommentList = async (articleId: number) => {
    let commentList = <IComment[]>[];
    try {
      const response = await axios.get(`/articles/${articleId}/comments`, {});
      commentList = {
        ...response.data,
      };
    } catch (error) {
      console.error(error);
    }
    return commentList;
  };

  return {
    getCommentList,
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
