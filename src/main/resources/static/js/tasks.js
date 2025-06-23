document.addEventListener('DOMContentLoaded', () => {
    const calendar = document.getElementById('calendar');
    let dayCells = {};
    let currentYear = new Date().getFullYear();
    let currentMonth = new Date().getMonth();

    function updateCurrentYearMonth() {
        const title = document.getElementById('calendar-title');
        if (title) {
            const m = title.textContent.match(/(\d+)年(\d+)月/);
            if (m) {
                currentYear = parseInt(m[1], 10);
                currentMonth = parseInt(m[2], 10) - 1; // zero-based month
            }
        }
    }

    function mapDayCells() {
        dayCells = {};
        updateCurrentYearMonth();
        calendar.querySelectorAll('td').forEach(td => {
            const day = parseInt(td.textContent, 10);
            if (!isNaN(day)) {
                dayCells[day] = td;
            }
        });
    }

    function initTasks() {
        mapDayCells();
        document.querySelectorAll('.task-confirm-box').forEach(cb => {
            if (cb.checked) {
                addTask(cb.dataset.name, cb.dataset.date);
            }
        });
    }

    function addTask(name, dateStr) {
        const date = new Date(dateStr);
        if (date.getFullYear() !== currentYear || date.getMonth() !== currentMonth) {
            return;
        }
        const cell = dayCells[date.getDate()];
        if (!cell) return;
        let wrapper = cell.querySelector('.tasks');
        if (!wrapper) {
            wrapper = document.createElement('div');
            wrapper.className = 'tasks';
            cell.appendChild(wrapper);
        }
        const item = document.createElement('div');
        item.textContent = name.slice(0, 4);
        item.title = name;
        item.dataset.name = name;
        wrapper.appendChild(item);
    }

    function removeTask(name, dateStr) {
        const date = new Date(dateStr);
        if (date.getFullYear() !== currentYear || date.getMonth() !== currentMonth) {
            return;
        }
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

    document.querySelectorAll('.due-date-input').forEach(inp => {
        inp.addEventListener('change', () => {
            const data = {
                taskName: inp.dataset.name,
                dueDate: inp.value
            };
            fetch('/task-due-date', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            });

            const cb = document.querySelector('.task-confirm-box[data-name="' + inp.dataset.name + '"]');
            if (cb) {
                if (cb.checked) {
                    removeTask(cb.dataset.name, cb.dataset.date);
                    addTask(cb.dataset.name, inp.value);
                }
                cb.dataset.date = inp.value;
            }
        });
    });

    document.querySelectorAll('.task-name-input').forEach(inp => {
        inp.addEventListener('change', () => {
            const data = {
                oldTaskName: inp.dataset.oldName,
                newTaskName: inp.value
            };
            fetch('/task-name', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            });

            const cb = document.querySelector('.task-confirm-box[data-name="' + inp.dataset.oldName + '"]');
            if (cb) {
                if (cb.checked) {
                    removeTask(cb.dataset.name, cb.dataset.date);
                    addTask(inp.value, cb.dataset.date);
                }
                cb.dataset.name = inp.value;
            }

            const dd = document.querySelector('.due-date-input[data-name="' + inp.dataset.oldName + '"]');
            if (dd) {
                dd.dataset.name = inp.value;
            }

            inp.dataset.oldName = inp.value;
        });
    });

    initTasks();
    document.addEventListener('calendarRendered', initTasks);
});

