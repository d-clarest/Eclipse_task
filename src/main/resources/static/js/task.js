document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.confirm-checkbox').forEach(cb => {
        cb.addEventListener('change', event => {
            const name = event.target.getAttribute('data-name');
            const checked = event.target.checked;
            fetch('/task/confirm', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: `name=${encodeURIComponent(name)}&checked=${checked}`
            });
        });
    });
});
