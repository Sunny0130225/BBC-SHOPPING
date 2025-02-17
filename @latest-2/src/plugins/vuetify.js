// plugins/vuetify.js
import { createVuetify } from "vuetify";
import * as components from "vuetify/components";
import * as directives from "vuetify/directives";
import "vuetify/styles"; // 引入 Vuetify 樣式



const vuetify = createVuetify({
  components,
  directives,
});

export default vuetify;
