import { computed, Ref, ref } from "vue";

interface PaginationConfig<T> {
  rowsPerPage?: Ref<number>;
  pagesPerBlock?: Ref<number>;
  totalCount?: Ref<number>;
  currentPage: Ref<number>;
}

export function usePagination<T>(config: PaginationConfig<T>) {
  const rowsPerPage = config.rowsPerPage || ref(5);
  const totalCount = config.totalCount || ref(0);

  const numberOfPagesArray = computed(() =>Array.from(
      { length: (stop - start) / 1 + 1 },
      (value, index) => start + index * 1
    )
  );

  return {
    numberOfPagesArray
  };
}
