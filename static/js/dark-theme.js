
     const toggleBtn = document.getElementById('theme-toggle');
      const root = document.documentElement;

      // Применяем тему, читая из localStorage при загрузке страницы
      const currentTheme = localStorage.getItem('theme') || 'light';
      applyTheme(currentTheme);
      updateButtonText(currentTheme);

      toggleBtn.addEventListener('click', () => {
        const theme = root.getAttribute('data-theme') === 'dark' ? 'light' : 'dark';
        applyTheme(theme);
        localStorage.setItem('theme', theme);
        updateButtonText(theme);
      });

      function applyTheme(theme) {
        if (theme === 'dark') {
          root.setAttribute('data-theme', 'dark');
        } else {
          root.removeAttribute('data-theme');
        }
      }

      function updateButtonText(theme) {
        toggleBtn.textContent = theme === 'dark' ? '☀️ Light theme' : '🌙 Dark theme';
      }
