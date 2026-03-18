module.exports = {
  content: {
    files: [ "SOURCES" ]
  },
  theme: {
    extend: {
      maxWidth: {
        '8xl': '95rem',
      },
      colors: {
        'ai-rainbow': {
          'red': '#ff006e',
          'orange': '#fb5607',
          'yellow': '#ffbe0b',
          'green': '#06ffa5',
          'blue': '#3a86ff',
          'purple': '#8338ec',
          'pink': '#ff006e',
        },
        'ai-cyber': {
          'cyan': '#00ffff',
          'magenta': '#ff00ff',
          'lime': '#00ff00',
        }
      },
      animation: {
        'rainbow': 'rainbow 15s ease infinite',
        'float': 'float 6s ease-in-out infinite',
        'glow': 'pulse-glow 2s ease-in-out infinite',
        'text-rainbow': 'text-rainbow 3s linear infinite',
        'particle': 'particle-float linear infinite',
        'glitch': 'glitch 500ms infinite',
      },
      keyframes: {
        rainbow: {
          '0%': { backgroundPosition: '0% 50%' },
          '50%': { backgroundPosition: '100% 50%' },
          '100%': { backgroundPosition: '0% 50%' },
        },
        float: {
          '0%, 100%': { transform: 'translateY(0px) rotate(0deg)' },
          '25%': { transform: 'translateY(-20px) rotate(1deg)' },
          '75%': { transform: 'translateY(10px) rotate(-1deg)' },
        },
        'pulse-glow': {
          '0%, 100%': { boxShadow: '0 0 20px rgba(255, 0, 255, 0.5), 0 0 40px rgba(0, 255, 255, 0.3)' },
          '50%': { boxShadow: '0 0 30px rgba(255, 0, 255, 0.8), 0 0 60px rgba(0, 255, 255, 0.5)' },
        },
        'text-rainbow': {
          '0%': { color: '#ff0000', textShadow: '0 0 10px #ff0000' },
          '16.66%': { color: '#ff8800', textShadow: '0 0 10px #ff8800' },
          '33.33%': { color: '#ffff00', textShadow: '0 0 10px #ffff00' },
          '50%': { color: '#00ff00', textShadow: '0 0 10px #00ff00' },
          '66.66%': { color: '#0088ff', textShadow: '0 0 10px #0088ff' },
          '83.33%': { color: '#8800ff', textShadow: '0 0 10px #8800ff' },
          '100%': { color: '#ff0000', textShadow: '0 0 10px #ff0000' },
        },
        'particle-float': {
          '0%': { transform: 'translateY(100vh) rotate(0deg)', opacity: '0' },
          '10%': { opacity: '1' },
          '90%': { opacity: '1' },
          '100%': { transform: 'translateY(-100vh) rotate(720deg)', opacity: '0' },
        }
      }
    }
  }
}
