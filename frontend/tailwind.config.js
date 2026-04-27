/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}"
  ],
  theme: {
    extend: {
      colors: {
        coffee: {
          100: '#fdf3e6',
          200: '#fae8d3',
          300: '#f5d4b0',
          400: '#efb88a',
          500: '#e8985f',
          600: '#d97d3e',
          700: '#c46830',
          800: '#9d5428',
          900: '#6b3a1a',
        },
        cream: '#fef9f3',
        espresso: '#2b1810'
      }
    }
  },
  plugins: []
}