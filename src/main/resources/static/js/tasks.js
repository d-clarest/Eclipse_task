document.addEventListener('DOMContentLoaded', () => {
    const calendar = document.getElementById('calendar');
    const dayCells = {};
    calendar.querySelectorAll('td').forEach(td => {
        const day = parseInt(td.textContent, 10);
        if (!isNaN(day)) {
            dayCells[day] = td;
        }
    });

    function addTask(name, dateStr) {
        const date = new Date(dateStr);
        const cell = dayCells[date.getDate()];
        if (!cell) return;
        let wrapper = cell.querySelector('.tasks');
        if (!wrapper) {
            wrapper = document.createElement('div');
            wrapper.className = 'tasks';
            cell.appendChild(wrapper);
        }
        const item = document.createElement('div');
        item.textContent = name;
        item.dataset.name = name;
        wrapper.appendChild(item);
    }

    function removeTask(name, dateStr) {
        const date = new Date(dateStr);
        const cell = dayCells[date.getDate()];
        if (!cell) return;
        const wrapper = cell.querySelector('.tasks');
        if (!wrapper) return;
        wrapper.querySelectorAll('div').forEach(div => {
            if (div.dataset.name === name) {
                div.remove();
            }
        });
    }

    document.querySelectorAll('.task-confirm-box').forEach(cb => {
        if (cb.checked) {
            addTask(cb.dataset.name, cb.dataset.date);
        }

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

            if (cb.checked) {
                addTask(cb.dataset.name, cb.dataset.date);
            } else {
                removeTask(cb.dataset.name, cb.dataset.date);
            }
        });
    });
});
