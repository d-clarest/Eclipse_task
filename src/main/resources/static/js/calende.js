// Calendar script from task-top.html

document.addEventListener('DOMContentLoaded', function() {
    const container = document.getElementById('calendar');
    const today = new Date();
    const year = today.getFullYear();
    const month = today.getMonth(); // 0-based

    // Monday を週の開始とするため、
    // getDay() の結果 (0:日曜) を1つ左へシフト
    const firstDay = (new Date(year, month, 1).getDay() + 6) % 7;
    const lastDate = new Date(year, month + 1, 0).getDate();

    let html = '<table>';
    html += '<tr><th colspan="7">' + year + '年' + (month + 1) + '月</th></tr>';
    // 表示順を月曜始まりに変更
    html += '<tr><th>月</th><th>火</th><th>水</th><th>木</th><th>金</th><th>土</th><th>日</th></tr><tr>';

    for (let i = 0; i < firstDay; i++) {
        html += '<td></td>';
    }

    for (let day = 1; day <= lastDate; day++) {
        if ((firstDay + day - 1) % 7 === 0 && day !== 1) {
            html += '</tr><tr>';
        }
        const isToday = day === today.getDate();
        const cls = isToday ? ' class="today"' : '';
        html += '<td' + cls + '>' + day + '</td>';
    }

    const remaining = (firstDay + lastDate) % 7;
    if (remaining !== 0) {
        for (let i = 0; i < 7 - remaining; i++) {
            html += '<td></td>';
        }
    }
    html += '</tr></table>';
    container.innerHTML = html;
});
