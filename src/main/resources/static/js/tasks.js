document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.task-confirm-box').forEach(cb => {
        cb.addEventListener('change', () => {
            const data = {
                taskName: cb.dataset.name,
                dueDate: cb.dataset.date,
                confirmed: cb.checked
            };
            fetch('/task-confirm', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            });
        });
    });
});
