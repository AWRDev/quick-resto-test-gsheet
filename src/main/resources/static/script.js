    const localIP = window.location.hostname
    alert("Вводите данные в ячейки по одному, после ввода числа или формулы в ячейку, нажмите Enter\nЕсли после нажатия Enter ничего не произошло, обновите страницу");
    function generateTable(cols, rows) {
        const table = document.createElement("table");
        table.setAttribute('border', '1');

        let table_info = ""

        // create row for column names
        const headRow = document.createElement("tr");
        headRow.appendChild(document.createElement("th")); // empty cell in top left corner

        for (let i = 0; i < cols; i++) {
            const colName = String.fromCharCode(65 + i); // convert 0-based index to A-Z
            const cell = document.createElement("th");
            cell.textContent = colName;
            headRow.appendChild(cell);
        }
        table.appendChild(headRow);

        // create remaining rows
        for (let i = 0; i < rows; i++) {
            const row = document.createElement("tr");
            const rowNumber = i + 1; // convert 0-based index to 1-based row number
            const headerCell = document.createElement("th");
            headerCell.textContent = rowNumber;
            row.appendChild(headerCell);

            for (let j = 0; j < cols; j++) {
                const cell = document.createElement("td");
                cell.setAttribute("class", "table-cell")
                cellId = String.fromCharCode(65 + j) + (i + 1)
                cell.setAttribute("id", cellId)
                cell.onclick = function(){
                    loadCellInfo(cell.id)
                }
                row.appendChild(cell);
            }
            table.appendChild(row);
        }
        return table;
    }

    const table = generateTable(4, 4);
    document.body.appendChild(table);
    btn_clearTable = document.createElement("button")
    btn_clearTable.innerHTML = "Clear"
    btn_clearTable.onclick = function(){
            clearTable()
            setTimeout(getTable, 500)
        }
    document.body.appendChild(btn_clearTable);
    info = document.getElementById("info")
    margin = parseInt(window.getComputedStyle(document.body).marginLeft)
    info.style.width=`${table.offsetWidth-margin}px`
    cells = document.getElementsByClassName("table-cell")
    Array.prototype.forEach.call(cells, function(cell) {
        cell.setAttribute("contenteditable", true);
        cell.addEventListener("keydown", myFunction);
    });


    getTable()

    function loadTable(data) {
        table_info = data
        dataFlattened = [].concat.apply([], data.cellValues)
        let i = 0;
        Array.prototype.forEach.call(cells, function(cell) {
            cell.innerHTML = dataFlattened[i];
            i++;
        });
    }

    function loadCell(cellId) {
        fetch(`http://${localIP}:8080/api/v1/table/${cellId}`)
            .then(response => response.json())
            .then(data => {
                console.log(data);
                updateCell(cellId, data)
            })
            .catch(error => console.error(error));
    }
    function getNumericCellId(cellId) {
      const colNum = cellId.charCodeAt(0) - 'A'.charCodeAt(0);
      const rowNum = parseInt(cellId.substring(1)) - 1;
      const numericCellId = [rowNum, colNum];
      return numericCellId;
    }
    function loadCellInfo(cellId){
        info = document.getElementById("info")
        numericCellId = getNumericCellId(cellId)
        cellFormula = table_info.cellFormula[numericCellId[0]][numericCellId[1]]
        if (cellFormula != ""){
            info.value = cellFormula
        }
        else{
            info.value = table_info.cellValues[numericCellId[0]][numericCellId[1]]
        }

    }
    function updateCell(cellId, data) {
        document.getElementById(cellId).innerHTML = data
    }

    function updateTable(cellId, newValue) {
        fetch(`http://${localIP}:8080/api/v1/table/a`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    cellId: cellId,
                    newValue: newValue
                })
            })
            .then(response => {
                console.log('Success:', response);
            })
            .catch(error => {
                console.error('Error:', error);
            })
    }

    function getTable() {
        fetch(`http://${localIP}:8080/api/v1/table`)
            .then(response => response.json())
            .then(data => {
                console.log(data);
                loadTable(data)
            })
            .catch(error => console.error(error));
    }
    function clearTable(){
        fetch(`http://${localIP}:8080/api/v1/table/clearTable`)
            .then(response => console.log('Success:', response))
            .catch(error => console.error(error));
    }
    function myFunction(event) {
        if (event.key === "Enter") {
            console.log(event)
            event.srcElement.blur()
            updateTable(event.srcElement.id, event.srcElement.innerHTML)
                //loadCell(event.srcElement.id)
            setTimeout(getTable, 500)
        }
    }