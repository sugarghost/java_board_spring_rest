// Styles
import "@mdi/font/css/materialdesignicons.css";
import "vuetify/dist/vuetify.min.css";

// Vuetify
import { createVuetify } from "vuetify";
import { VDataTable,VDataTableServer } from "vuetify/labs/VDataTable"

export default createVuetify({
    components: {
        VDataTable,
        VDataTableServer
    },
});
