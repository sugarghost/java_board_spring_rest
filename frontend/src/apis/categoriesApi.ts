import { ref, Ref } from "vue";
import axios from "../axios/axios";
import type { ICategory } from "../types/category";

export default function categoriesApi() {
  const categories: Ref<ICategory[]> = ref([]);

  const getCategories = async () => {
    try {
      const response = await axios.get("/categories", {});

      categories.value = response.data;
    } catch (error) {
      console.error(error);
    }
  };

  return {
    categories,
    getCategories,
  };
}
