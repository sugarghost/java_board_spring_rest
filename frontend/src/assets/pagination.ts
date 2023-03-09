import { computed, Ref, ref } from "vue";

interface PaginationConfig<T> {
  rowsPerPage?: Ref<number>;
  pagesPerBlock?: Ref<number>;
  totalArticleCount: Ref<number>;
  currentPage: Ref<number>;
}

export default function usePagination<T>(config: PaginationConfig<T>) {
  const rowsPerPage = config.rowsPerPage || ref(5);
  const pagesPerBlock = config.pagesPerBlock || ref(10);
  const pageStart = computed(
    () => Math.floor((config.currentPage.value - 1) / pagesPerBlock.value) * pagesPerBlock.value + 1
  );
  const pageEnd = computed(() => pageStart.value + pagesPerBlock.value - 1);
  const totalPageCount = computed(() =>
    Math.ceil(config.totalArticleCount.value / rowsPerPage.value)
  );

  return {
    pageStart,
    pageEnd,
    totalPageCount,
  };
}
