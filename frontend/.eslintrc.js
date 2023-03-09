module.exports = {
  root: true,
  env: {
    node: true,
  },
  plugins: ["prettier"],
  extends: ["plugin:vue/vue3-essential", "@vue/airbnb", "@vue/typescript/recommended", "prettier"],
  parserOptions: {
    ecmaVersion: 2020,
  },
  rules: {
    "vuejs-accessibility/click-events-have-key-events": "off",
    "vuejs-accessibility/form-control-has-label": "off",
    "vuejs-accessibility/label-has-for": "off",
    quotes: [2, "double"],
    "linebreak-style": 0,
    "no-console": process.env.NODE_ENV === "production" ? "warn" : "off",
    "no-debugger": process.env.NODE_ENV === "production" ? "warn" : "off",
  },
};
