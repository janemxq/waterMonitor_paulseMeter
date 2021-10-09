module.exports = {
  root: true,
  'extends': [
    'plugin:vue/essential',
    '@vue/standard'
  ],
  rules: {
    // allow async-await
    'generator-star-spacing': 'off',
    // allow debugger during development
    'no-debugger': process.env.NODE_ENV === 'production' ? 'error' : 'off',
    'vue/no-parsing-error': [2, {
      'x-invalid-end-tag': false
    }],
    'no-undef': 'off',
    'camelcase': 'off',
    'eslint-disable-next-line': 'off',
    'prefer-promise-reject-errors': 'off',
    'standard/no-callback-literal': 'off',
    'no-control-regex': 'off',
    'comma-dangle': 'off',
    'vue/valid-v-for': 'off',
    'vue/require-v-for-key': 'off',
    'quotes': 'off'
  },
  parserOptions: {
    parser: 'babel-eslint'
  }
}
