   console.log('dark-theme script is on');
    function applyTheme(theme) {
      document.body.classList.remove('light-theme', 'dark-theme');
      document.body.classList.add(theme + '-theme');
    }

    const savedTheme = localStorage.getItem('theme') || 'light';

    applyTheme(savedTheme);


    document.getElementById('toggle-theme').addEventListener('click', () => {
      const currentTheme = document.body.classList.contains('dark-theme') ? 'dark' : 'light';
      const newTheme = currentTheme === 'dark' ? 'light' : 'dark';
      applyTheme(newTheme);
      localStorage.setItem('theme', newTheme);
    });