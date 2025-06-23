document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.schedule-date-input').forEach(inp => {
        inp.addEventListener('change', () => {
            const date = new Date(inp.value);
            if (isNaN(date.getTime())) return;
            const dow = date.toLocaleDateString('ja-JP', { weekday: 'short' });
            const row = inp.closest('tr');
            if (row) {
                const dayField = row.querySelector('.schedule-day-of-week');
                if (dayField) {
                    dayField.value = dow;
                }
            }
        });
    });
});
