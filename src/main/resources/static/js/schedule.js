//ページが全部読み込まれたら、中の処理を始める
document.addEventListener('DOMContentLoaded', () => {
  const completedTable = document.getElementById('completed-table'); //schedule-box.htmlの完了済みテーブル
  const upcomingTable = document.getElementById('upcoming-table'); //schedule-box.htmlのこれからの予定テーブル
  const calendar = document.getElementById('calendar');
  let dayCells = {}; //連想配列の定義
  let currentYear = new Date().getFullYear();
  let currentMonth = new Date().getMonth();
  const newButton = document.getElementById('new-schedule-button');
  const toggleBtn = document.getElementById('toggle-completed-button');
  const STORAGE_KEY = 'completedShown';
  let completedShown = localStorage.getItem(STORAGE_KEY) === 'true'; //
  let displayedCompleted = [];
  const pointDisplay = document.getElementById('total-point-display');
  enableFullTextDisplay('.schedule-title-input, .schedule-location-input, .schedule-detail-input, .schedule-feedback-input');
  refreshTotalPoint(); //ページにポイント表示する部分があれば，表示
  sortAllTables(); //予定データベーステーブルを日付と時間の昇順に並べ替える,初期化
  initSchedules(); //今表示されているカレンダーに予定を埋め込む
  document.addEventListener('calendarRendered', initSchedules); //calendarRendered というイベントが起きたら initSchedules 関数を実行．つまり，カレンダーの描画が終わってから，予定を埋め込むということ．

  //このif文はnewButtonがnullの時の可能性もあるので必要，nullの時，newButton.addEventListenerでエラーになる
  if (newButton) {
    // 予定新規が押されたら
    newButton.addEventListener('click', () => {
      const today = new Date(); //年・月・日・時・分・秒 などの情報がすべて入っている
      const dateStr = today.toISOString().split('T')[0]; //toISOString() → 2025-06-26T10:30:00.000Z のような文字列になる,.split('T')[0] → "2025-06-26" の部分だけ取り出す
      const dow = today.toLocaleDateString('ja-JP', { weekday: 'short' }); //今日の曜日を日本語で取得，火，木みたいな
      const data = {
        addFlag: true,
        title: '',
        dayOfWeek: dow,
        scheduleDate: dateStr,
        startTime: '12:00',
        endTime: '18:00',
        location: '',
        detail: '',
        feedback: '',
        point: 1,
        completedDay: null,
      };
      //'/schedule-add' というURLにJSON形式でdataを文字列に変換してPOST，データの送信が成功したら，ページ再読み込み
      fetch('/schedule-add', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data), //@RequestBodyでjava側でjsonでPOSTしたやつをjava側で
      }).then(() => {
        location.reload(); //dataの中のlocationとは全く関係のない
      });
    });
  }

  //削除ボタンが押されたら
  document.querySelectorAll('.delete-button').forEach((btn) => {
    btn.addEventListener('click', () => {
      const row = btn.closest('tr');
      if (!row) return;
      const id = row.dataset.id; //task-top.html側でth:data-id="${schedule.id}"があるから使える
      if (!id) return;
      fetch('/schedule-delete', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ id: parseInt(id, 10) }), //parseInt(id, 10)でidを10進数の整数に変換,id:とすることで、entityのScheduleのidを設定している
      }).then(() => {
        location.reload();
      });
    });
  });

  //addプロパティにチェックの操作をしたら
  document.querySelectorAll('.schedule-add-flag').forEach((cb) => {
    cb.addEventListener('change', () => {
      const row = cb.closest('tr');
      if (!row) return;
      const title = row.querySelector('.schedule-title-input').value;
      const date = row.querySelector('.schedule-date-input').value;
      const id = row.querySelector('.schedule-id').value;
      //チェックがついているなら
      if (cb.checked) {
        const times = getStartEnd(row); //連想配列でstart時間とend時間を取得
        addSchedule(title, date, { id, start: times.start, end: times.end }); //カレンダーに予定を追加
      } else {
        removeSchedule(id, date);
      }
      sendUpdate(row);
    });
  });

  // 完了ボタンが押されたら
  document.querySelectorAll('.complete-button').forEach((btn) => {
    const row = btn.closest('tr'); //ボタンのある行を取得
    const comp = row ? row.querySelector('.completed-day-input') : null; //その行にrowがあれば、完了日をcompに代入
    //もし完了日に値があるなら、取消ボタンに変更
    if (comp && comp.value) {
      btn.value = '取消';
    }
    //完了ボタンを押したら
    btn.addEventListener('click', () => {
      if (!row || !comp) return;
      // もし完了ボタンなら
      if (btn.value === '完了') {
        const today = new Date().toISOString().split('T')[0];
        comp.value = today; //今日の日付を完了日に
        btn.value = '取消';
        updateCalendarCompletion(row, true); //今表示中のやつ一回非表示にして、赤文字でカレンダーに表示
        moveRowBasedOnCompletion(row, true); //表面上の予定テーブルの更新
        const span = row.querySelector('td:last-child span');
        //開始までの時間に何か入っているなら、nullに
        if (span) span.textContent = '';
        //取消ボタンが押されたら
      } else {
        comp.value = ''; //
        btn.value = '完了';
        updateCalendarCompletion(row, false); //今表示中のやつ一回非表示にして,黒文字でカレンダーに追加
        moveRowBasedOnCompletion(row, false); //表面上の予定テーブル更新
        updateTimeUntilStart(row); //開始までの時間を算出
      }
      sendUpdate(row); //バック側のデータベースも更新
    });
  });

  //予定名が変更されたら
  document.querySelectorAll('.schedule-title-input').forEach((inp) => {
    const handler = () => {
      const row = inp.closest('tr');
      if (row) {
        const cb = row.querySelector('.schedule-add-flag');
        //addプロパティにチェックがついているなら
        if (cb && cb.checked) {
          removeSchedule(row.dataset.id, row.dataset.oldDate); //一回カレンダーから除外して
          const times = getStartEnd(row);
          addSchedule(inp.value, row.querySelector('.schedule-date-input').value, { id: row.dataset.id, start: times.start, end: times.end });
        }
        sendUpdate(row); //サーバサイドのデータベースを更新
      }
    };
    inp.addEventListener('change', handler);
  });

  //日付が変更されたら
  document.querySelectorAll('.schedule-date-input').forEach((inp) => {
    const handler = () => {
      const date = new Date(inp.value);
      if (isNaN(date.getTime())) return;
      const dow = date.toLocaleDateString('ja-JP', { weekday: 'short' }); //曜日
      const row = inp.closest('tr');
      if (row) {
        const dayField = row.querySelector('.schedule-day-of-week'); //曜日のやつ
        if (dayField) {
          dayField.value = dow;
        }
        const cb = row.querySelector('.schedule-add-flag');
        //addにチェックついているなら
        if (cb && cb.checked) {
          removeSchedule(row.dataset.id, row.dataset.oldDate); //一回カレンダーから除外して
          const times = getStartEnd(row);
          addSchedule(row.querySelector('.schedule-title-input').value, inp.value, { id: row.dataset.id, start: times.start, end: times.end });
        }
        updateTimeUntilStart(row); //開始までの時間を更新
        sortAllTables(); //予定を日付順に並び替え
        sendUpdate(row); //サーバサイドのデータベースを更新
      }
    };
    inp.addEventListener('change', handler);
  });

  //場所、詳細、feedback、ポイントに入力されたら
  ['.schedule-location-input', '.schedule-detail-input', '.schedule-feedback-input', '.point-input'].forEach((selector) => {
    document.querySelectorAll(selector).forEach((inp) => {
      inp.addEventListener('change', () => {
        const row = inp.closest('tr');
        if (row) sendUpdate(row);
      });
    });
  });

  //スタート時間の時間または分が変更されたら
  document.querySelectorAll('.start-hour').forEach((sel) => {
    const minuteSel = sel.parentElement.querySelector('.start-minute');
    initTimeSelects(sel, minuteSel, sel.dataset.time); //ここで選択欄を作成
    //スタートの時間が変更されたら
    sel.addEventListener('change', () => {
      const row = sel.closest('tr');
      if (row) {
        sendUpdate(row); //サーバーサイド更新
        updateTimeUntilStart(row); //開始までの時間変更
        sortAllTables(); //日付＆時間順に並べ替え
      }
    });
    //スタートの分が変更されたら
    if (minuteSel) {
      minuteSel.addEventListener('change', () => {
        const row = sel.closest('tr');
        if (row) {
          sendUpdate(row); //サーバーサイド更新
          updateTimeUntilStart(row); //開始までの時間変更
          sortAllTables(); //日付＆時間・分順に並べ替え
        }
      });
    }
  });

  //エンド時間の時間または分が変更されたら
  document.querySelectorAll('.end-hour').forEach((sel) => {
    const minuteSel = sel.parentElement.querySelector('.end-minute');
    initTimeSelects(sel, minuteSel, sel.dataset.time); //ここで選択欄を作成
    //エンドの時間が変更されたら
    sel.addEventListener('change', () => {
      const row = sel.closest('tr');
      if (row) sendUpdate(row); //サーバーサイド更新
    });
    //エンドの分が更新されたら
    if (minuteSel) {
      minuteSel.addEventListener('change', () => {
        const row = sel.closest('tr');
        if (row) sendUpdate(row); //サーバーサイド更新
      });
    }
  });

  //完了日が入力されると
  document.querySelectorAll('.completed-day-input').forEach((inp) => {
    const handler = () => {
      const row = inp.closest('tr');
      if (!row) return;
      const completed = !!inp.value; //値があればtrue
      updateCalendarCompletion(row, completed); //今表示中のやつ一回非表示にして,赤文字ででカレンダーに追加
      sendUpdate(row); //サーバサイドのデータベースを更新
      moveRowBasedOnCompletion(row, completed); //予定テーブルの移動や表示（表面上の）
      //完了日があるなら
      if (completed) {
        const span = row.querySelector('td:last-child span');
        if (span) span.textContent = ''; //開始までをnullに
      } else {
        updateTimeUntilStart(row); //開始までの時間を表示
      }
    };
    inp.addEventListener('input', handler);
  });

  //表示/非表示ボタンが押されたら、toggleBtnが入っていないページでのエラーを防ぐ
  if (toggleBtn) {
    toggleBtn.addEventListener('click', () => {
      //いまページに「表示」と表示されている場合はelseの方に、「非表示」と表示されていればifの方に
      if (completedShown) {
        removeDisplayedCompleted(); //予定完了済みのdisplayedCompletedから1つずつ取り出して、カレンダーから取り除く、そしてdisplayedCompletedを空にする
        toggleBtn.textContent = '表示';
        completedShown = false;
      } else {
        fetchCompletedAndShow(); //予定完了済み　かつ　現在のカレンダーの年月でフィルターをかけて予定追加（赤文字で）
        toggleBtn.textContent = '非表示';
        completedShown = true;
      }
      localStorage.setItem(STORAGE_KEY, completedShown); //ローカルに保持、ユーザーのブラウザにデータ（文字列）をキーと値のペアで保存
    });
  }

  //ページ読み込み時に前回の表示状態を復元、completedShownがtrueというのは、今ボタンが非表示の時。
  if (toggleBtn && completedShown) {
    toggleBtn.textContent = '非表示';
    fetchCompletedAndShow(); //予定完了済み　かつ　現在のカレンダーの年月でフィルターをかけて予定追加
    localStorage.setItem(STORAGE_KEY, 'true'); //ローカルに保持、次またページ更新しても保持するために
  }

  //カレンダーが読みこまれ(先月のカレンダーに移行したりすると)、かつcompletedShownがtrueの時は処理実行
  document.addEventListener('calendarRendered', () => {
    if (completedShown) {
      removeDisplayedCompleted(); //一旦、今のやつ全部破棄
      fetchCompletedAndShow(); //今表示のカレンダーで完了済みの予定を全部表示
    }
  });

  //日付が速い順にソート
  function sortScheduleTable(table) {
    if (!table) return; //引数が無効の場合の安全対策
    const tbody = table.tBodies[0] || table; //テーブルの中には通常 <tbody> があります.tBodies[0] は「最初の <tbody>」を指し，なければ table 自体を使う．
    const rows = Array.from(tbody.querySelectorAll('tr.schedule-row')); //tbody の中から、クラス名が "schedule-row" の <tr> 要素を全部取り出す。Array.from(...) で配列に変換し，ソートできるようにしている．
    //配列のsortメソッドは，2つの要素を何度も何度も比べて順番を入れ替える（今回は昇順）
    rows.sort((a, b) => {
      const dateA = a.querySelector('.schedule-date-input').value;
      const dateB = b.querySelector('.schedule-date-input').value;
      if (dateA < dateB) return -1; //そのまま
      if (dateA > dateB) return 1; //前後交換
      const tA = a.querySelector('.start-hour').value.padStart(2, '0') + ':' + a.querySelector('.start-minute').value.padStart(2, '0');
      const tB = b.querySelector('.start-hour').value.padStart(2, '0') + ':' + b.querySelector('.start-minute').value.padStart(2, '0');
      return tA.localeCompare(tB); //上の比較と同じ意味，-1,0,1のいずれかを返す．ただ，文字列比較をしている．:は必要．HH:MMにして時間順に比較している．
    });
    rows.forEach((r) => tbody.appendChild(r)); //html側にも反映させるためにtbodyに入れていっている．
  }

  function sortAllTables() {
    //classのdatabase-tableすべてに対して、それぞれsortScheduleTable関数を呼び出している、引数はforEachによって渡されている
    document.querySelectorAll('.database-table').forEach(sortScheduleTable);
  }

  //指定したtableにrowをぶち込む
  function moveRow(row, table) {
    if (!row || !table) return;
    const tbody = table.tBodies[0] || table;
    tbody.appendChild(row);
  }

  //予定テーブル関係の関数
  function moveRowBasedOnCompletion(row, completed) {
    // 予定ボックス間での移動なら
    if (completedTable && upcomingTable) {
      const target = completed ? completedTable : upcomingTable;
      moveRow(row, target); //予定ボックス間でに移動
      sortScheduleTable(target);
    } else {
      // top画面で完了済みなら非表示、未完了なら表示
      row.style.display = completed ? 'none' : '';
    }
  }

  //予定データベースを更新
  function sendUpdate(row) {
    const data = {
      id: parseInt(row.dataset.id, 10),
      addFlag: row.querySelector('.schedule-add-flag').checked,
      title: row.querySelector('.schedule-title-input').value,
      dayOfWeek: row.querySelector('.schedule-day-of-week').value,
      scheduleDate: row.querySelector('.schedule-date-input').value,
      startTime: row.querySelector('.start-hour').value.padStart(2, '0') + ':' + row.querySelector('.start-minute').value.padStart(2, '0'),
      endTime: row.querySelector('.end-hour').value.padStart(2, '0') + ':' + row.querySelector('.end-minute').value.padStart(2, '0'),
      location: row.querySelector('.schedule-location-input').value,
      detail: row.querySelector('.schedule-detail-input').value,
      feedback: row.querySelector('.schedule-feedback-input').value,
      point: parseInt(row.querySelector('.point-input').value || '0', 10),
      completedDay: row.querySelector('.completed-day-input').value || null,
    };
    fetch('/schedule-update', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    }).then((res) => {
      if (res.ok) {
        refreshTotalPoint();
      } else {
        console.error('Schedule update failed');
      }
    });
  }

  //開始までの時間を表示する、または期限切れにする
  function updateTimeUntilStart(row) {
    const dateStr = row.querySelector('.schedule-date-input').value;
    const hourSel = row.querySelector('.start-hour');
    const minuteSel = row.querySelector('.start-minute');
    if (!dateStr || !hourSel || !minuteSel) return;
    const start = new Date(`${dateStr}T${hourSel.value.padStart(2, '0')}:${minuteSel.value.padStart(2, '0')}:00`);
    const now = new Date();
    let diff = Math.floor((start - now) / 60000); // minutes
    const expired = diff <= 0;
    if (diff < 0) diff = 0;
    diff = Math.floor(diff / 5) * 5;
    const days = Math.floor(diff / (60 * 24));
    const hours = Math.floor((diff % (60 * 24)) / 60);
    const mins = diff % 60;
    const span = row.querySelector('td:last-child span');
    if (span) {
      if (expired) {
        span.textContent = '期限切れ';
        span.style.color = 'red';
      } else {
        span.textContent = `${days}日${hours}時間${mins}分`;
        span.style.color = '';
      }
    }
  }

  //現在のカレンダーの年月を取得し、currentYear、currentMonthを更新
  function updateCurrentYearMonth() {
    const title = document.getElementById('calendar-title');
    if (title) {
      const m = title.textContent.match(/(\d+)年(\d+)月/); //mにcalender-titleの文字に一致するやつを取り出す．m[0]に2025年6月，m[1]に2025，m[2]に6みたいな感じで
      if (m) {
        currentYear = parseInt(m[1], 10); //勝手に8進数で解釈しないように，10進数であることを明記
        currentMonth = parseInt(m[2], 10) - 1; //月を引数として使うときのために，-1しておく．0基準に合わせる．
      }
    }
  }

  //カレンダーに日付が入ったHTMLを作成
  function mapDayCells() {
    dayCells = {}; //連想配列の初期化
    updateCurrentYearMonth(); //今カレンダーが表示されている年月を取得し，currentYear，currentMonthに代入
    calendar.querySelectorAll('td').forEach((td) => {
      const day = parseInt(td.textContent, 10);
      //日付が書かれてる奴だけ以下の条件に入る．空白を除きたい．
      if (!isNaN(day)) {
        dayCells[day] = td; //例えば，dayCells[1]=<td>1</td>みたいなのが入ってる．
      }
    });
  }

  //開始時間と終了時間を連想配列で
  function getStartEnd(row) {
    const sh = row.querySelector('.start-hour').value.padStart(2, '0');
    const sm = row.querySelector('.start-minute').value.padStart(2, '0');
    const eh = row.querySelector('.end-hour').value.padStart(2, '0');
    const em = row.querySelector('.end-minute').value.padStart(2, '0');
    return { start: `${sh}:${sm}`, end: `${eh}:${em}` };
  }

  //カレンダーに予定を追加
  function addSchedule(name, dateStr, opts = {}) {
    const date = new Date(dateStr); //文字列をDate型に変換
    if (isNaN(date.getTime())) return; //日付をミリ秒に変換して，変な数字じゃないか判定
    //.getMonth() は－1される
    if (date.getFullYear() !== currentYear || date.getMonth() !== currentMonth)
      //現在表示されているカレンダーの年月とデータベースの予定テーブルの年月が一致しているやつだけ追加．一致してないのは入れない．
      return;
    const cell = dayCells[date.getDate()]; //日付に対応する<td>1</td>みたいなのを代入
    console.log(cell);
    if (!cell) return;
    let wrapper = cell.querySelector('.schedules'); //cellには<td>1</td>の中に，classのschedulesがあるか検索
    //td要素の中にclass="schedules"はない場合は，作る
    if (!wrapper) {
      wrapper = document.createElement('div');
      wrapper.className = 'schedules';
      cell.appendChild(wrapper); //<div class="schedules"></div>をcellの子要素に
    }
    const item = document.createElement('div');
    item.textContent = name.slice(0, 6); //予定名を6文字まで表示
    item.title = name; //カレンダー表示用
    item.dataset.name = name; //属性値
    item.dataset.date = dateStr; //属性値
    if (opts.start) item.dataset.start = opts.start;
    if (opts.end) item.dataset.end = opts.end;
    if (opts.id !== undefined) {
      item.dataset.id = String(opts.id);
    }
    if (opts.completed) {
      item.style.color = 'red';
      item.dataset.completed = 'true';
    }
    wrapper.appendChild(item); //<div>name</div>をwrapperの子要素に
    return item;
  }

  //カレンダーの予定から取り除く
  function removeSchedule(id, dateStr) {
    const date = new Date(dateStr); //文字列をDate型に変換
    if (isNaN(date.getTime())) return;
    if (date.getFullYear() !== currentYear || date.getMonth() !== currentMonth) return;
    const cell = dayCells[date.getDate()];
    if (!cell) return;
    const wrapper = cell.querySelector('.schedules');
    if (!wrapper) return;
    const targetId = String(id);
    wrapper.querySelectorAll('div').forEach((div) => {
      if (div.dataset.id === targetId) {
        div.remove();
      }
    });
  }

  //カレンダーのベースを作って、未完了の予定を予定を入れる
  function initSchedules() {
    mapDayCells(); //calender-titleの年月の約30日分の日付連想配列dayCellsを作成
    //これからの予定
    document.querySelectorAll('.schedule-row').forEach((row) => {
      const cb = row.querySelector('.schedule-add-flag'); //cbには<input type="checkbox">が入る．
      const comp_btn = row.querySelector('.complete-button');
      //cb.checkedでチェックがついてて未完了の予定なら
      if (cb && cb.checked && comp_btn.value == '完了') {
        const name = row.querySelector('.schedule-title-input').value; //予定名
        const date = row.querySelector('.schedule-date-input').value; //何年何月何日
        const id = row.dataset.id;
        const times = getStartEnd(row);
        addSchedule(name, date, { id, start: times.start, end: times.end });
      }
    });
  }

  //開始時間と終了時間の時間と分をどのくらいの刻みで選択できるようにするか
  function initTimeSelects(hourSel, minuteSel, time) {
    for (let h = 0; h < 24; h++) {
      const opt = document.createElement('option');
      opt.value = String(h).padStart(2, '0');
      opt.textContent = opt.value;
      hourSel.appendChild(opt);
    }
    for (let m = 0; m < 60; m += 5) {
      const opt = document.createElement('option');
      opt.value = String(m).padStart(2, '0');
      opt.textContent = opt.value;
      minuteSel.appendChild(opt);
    }
    // 初期値が指定されていたら、その値にセット
    if (time) {
      const [h, m] = time.split(':');
      hourSel.value = h;
      minuteSel.value = m.padStart(2, '0');
    }
  }

  //入力欄を選択または、入力中に幅が自動で変わる
  function enableFullTextDisplay(selector) {
    document.querySelectorAll(selector).forEach((inp) => {
      let originalWidth = ''; //フォーカスが外れたときにこの幅に戻す
      //入力欄の幅を内容に合わせて調整する関数
      const adjustWidth = () => {
        inp.style.width = 'auto'; //幅を自動に設定し、幅の計算をリセット
        const w = inp.scrollWidth + 4; //「4px」の余裕を足して、文字が切れないように
        inp.style.width = w + 'px';
      };
      // フォーカスが当たった瞬間に、現在の幅を originalWidth に保存
      inp.addEventListener('focus', () => {
        originalWidth = inp.style.width || inp.offsetWidth + 'px'; //inp.style.width が設定されていればそれを使う.なければ、現在の見た目の幅である offsetWidth を使う。
        adjustWidth(); //幅を内容に合わせて拡げる
      });
      inp.addEventListener('input', adjustWidth); //ユーザーが文字を入力するたびに adjustWidth を呼び、入力内容に合わせて幅を動的に変更
      // フォーカスが外れたら、保存しておいた元の幅に戻す
      inp.addEventListener('blur', () => {
        inp.style.width = originalWidth;
      });
    });
  }

  //カレンダーに赤文字か黒文字かを表示する
  function updateCalendarCompletion(row, completed) {
    const cb = row.querySelector('.schedule-add-flag');
    if (!cb || !cb.checked) return;
    const title = row.querySelector('.schedule-title-input').value;
    const date = row.querySelector('.schedule-date-input').value;
    const id = row.querySelector('.schedule-id').value;
    const times = getStartEnd(row);
    removeSchedule(id, date); //完了済みであろうとなかろうと一旦レコードを取り除く
    if (completed) {
      //今カレンダーに過去の予定を表示するなら
      if (completedShown) {
        //赤文字でカレンダーに予定を追加（過去のものとして）
        addSchedule(title, date, {
          completed: true,
          id,
          start: times.start,
          end: times.end,
        });
        //
        if (!displayedCompleted.some((d) => d.id === id && d.date === date)) {
          //完了済みスケジュールのうちすでに画面に表示したものを記録
          displayedCompleted.push({ id, date });
        }
      }
    } else {
      //カレンダーが今過去のやつも非表示で、未完了の予定はカレンダーに黒で追加
      addSchedule(title, date, { id, start: times.start, end: times.end });
      //完了済みスケジュールのうちすでに画面に表示したものを記録
      displayedCompleted = displayedCompleted.filter((d) => !(d.id === id && d.date === date));
    }
  }

  //完了済みの予定を赤文字で表示する関数
  function fetchCompletedAndShow() {
    mapDayCells(); //calender-titleの年月の約30日分の日付連想配列dayCellsを作成
    const year = currentYear;
    const month = currentMonth + 1; //currentMonthはjs上の月なので、現実世界の月に変換
    fetch(`/completed-schedules?year=${year}&month=${month}`) //GETリクエスト
      .then((res) => res.json()) //データをjson形式で受け取る、次のthenに結果を返す
      .then((list) => {
        list.forEach((s) => {
          const start = (s.startTime || '').slice(0, 5);
          const end = (s.endTime || '').slice(0, 5);
          addSchedule(s.title, s.scheduleDate, {
            completed: true,
            id: s.id,
            start,
            end,
          }); //カレンダーに予定を追加
          displayedCompleted.push({ id: s.id, date: s.scheduleDate }); //完了済みスケジュールのうちすでに画面に表示したものを記録・管理するための配列
        });
      });
  }

  //一旦今のやつを全部
  function removeDisplayedCompleted() {
    displayedCompleted.forEach((d) => removeSchedule(d.id, d.date)); //一旦全部取り除く。カレンダーの表面上から
    displayedCompleted = [];
  }

  // 各データベースのすべてのポイントを取得（task-top.htmlで表示用）
  function refreshTotalPoint() {
    if (!pointDisplay) return; //ページにポイント表示するとことがない場合は，return
    fetch('/total-point')
      .then((res) => res.json()) //jsonでcontrollerにリクエスト
      .then((pt) => {
        pointDisplay.textContent = `${pt}P`; //controllerから受け取ったデータは，${}によって受け取る．
      });
  }
});
