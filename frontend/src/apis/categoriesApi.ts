import {ref, Ref} from "@vue/reactivity";
import axios from "axios";
import type {ICategory} from "../types/category";


export function categoriesApi() {
  const categories: Ref<ICategory[]> = ref([]);

  const getCategories = async () => {
    try {
      const response = await axios.get("/v1/categories", {});

      categories.value = response.data;
    } catch (error) {
      console.error(error);
    }
  };

  return {
    categories,
    getCategories
  };
}
