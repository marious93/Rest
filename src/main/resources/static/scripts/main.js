let URL = "http://localhost:8080/users"

async function loadPage() {
    await loadData()
    await fillRolesSelector()
    let a = fetchCurrentUser()
    document.getElementById("authorizedUser").innerText = await a;
}

async function loadData() {
    fetch(URL)
        .then(response => {
            if (!response.ok) {
                throw new Error(`Ошибка HTTP: ${response.status}`);
            }
            return response.json();
        })
        .then(async data => {
            await updateTableData(data);
        })
        .catch(error => {
            console.error('Произошла ошибка при запросе:', error);
        });
}

async function fetchCurrentUser() {
    try {
        const response = await fetch('users/current');
        if (response.ok) {
            const user = await response.json();
            return user.username + " with roles: " + user.rolesToString;
        } else {
            console.log('Не удалось получить данные пользователя. Статус:', response.status);
        }
    } catch (error) {
        console.error('Ошибка при запросе пользователя:', error);
    }
}

function updateTableData(data) {
    let table = document.getElementById('myTable')
    if (!table) {
        console.error("Таблица с id 'userTable' не найдена.");
        return;
    }
    const tableBody = document.getElementById('tbody')
    if (!tableBody) {
        console.error("Элемент <tbody> не найден внутри таблицы.");
        return;
    }
    while (tableBody.firstChild) {
        tableBody.removeChild(tableBody.firstChild);
    }
    data.forEach(user => {
        const row = tableBody.insertRow(); // Добавляем строку в tbody
        row.innerHTML = `
      <td>${user.id}</td>
      <td>${user.firstName}</td>
      <td>${user.lastName}</td>
      <td>${user.age}</td>
      <td>${user.username}</td>
      <td>${user.rolesToString}</td>
      <td><button class="edit-button btn btn-info text-white" data-id="${user.id}">Edit</button></td>
      <td><button class="delete-button btn btn-danger text-white" data-id="${user.id}">Delete</button></td>
    `;

        const editButton = row.querySelector('.edit-button');
        editButton.addEventListener('click', () => {
            const myModal = new bootstrap.Modal(document.getElementById('myModal'));
            myModal.show();
            const btn = document.getElementById('myForm1');
            let id = editButton.getAttribute('data-id')
            document.getElementById('firstName1').disabled = false;
            document.getElementById('lastName1').disabled = false;
            document.getElementById('age1').disabled = false;
            document.getElementById('username1').disabled = false;
            document.getElementById('password1').disabled = false;
            document.getElementById('roles1').disabled = false;
            document.getElementById('action').innerText = 'Edit';
            document.getElementById('exampleModalLabel').innerText = "Edit user"
            loadUser(id)
            btn.addEventListener('submit', async function (e) {
                e.preventDefault();
                if (id) {
                    let formData = collectingUserData(id)
                    fetch('/users', {
                        method: 'PUT',
                        body: formData
                    })
                        .then(response => {
                            if (response.ok) {
                                // $('#myModal').hide();
                                loadData();
                                $('#myModal').modal('hide')
                                hideTab2AndSwitchToTab1();
                            }
                        })
                        .catch((error) => {
                            window.alert(error.toString())
                        });
                }
            });

        });
        const deleteButton = row.querySelector('.delete-button');
        deleteButton.addEventListener('click', () => {
            const myModal = new bootstrap.Modal(document.getElementById('myModal'));
            myModal.show();
            let id = deleteButton.getAttribute('data-id')
            document.getElementById('firstName1').disabled = true;
            document.getElementById('lastName1').disabled = true;
            document.getElementById('age1').disabled = true;
            document.getElementById('username1').disabled = true;
            document.getElementById('password1').disabled = true;
            document.getElementById('roles1').disabled = true;
            document.getElementById('action').innerText = "Delete";
            document.getElementById('exampleModalLabel').innerText = "Delete user"
            loadUser(id)


            btn.addEventListener('submit', async function (e) {
                e.preventDefault();
                if (id) {
                    const formData = collectingUserData(id)
                    fetch("/users", {
                        method: 'DELETE',
                        body: formData,
                    })
                        .then(response => {
                            if (response.ok) {
                                loadData();
                                $('#myModal').modal('hide');
                                hideTab2AndSwitchToTab1();
                            }
                        })
                        .catch((error) => {
                            window.alert(error.toString())
                        });
                }
            });

        })
    });
}

function submitForm(event) {
    event.preventDefault();
    const form = document.getElementById('createForm');
    const formData = new FormData();
    formData.append("firstName", document.getElementById('firstName').value)
    formData.append("lastName", document.getElementById('lastName').value)
    formData.append("age", document.getElementById('age').value)
    formData.append("username", document.getElementById('username').value)
    formData.append("password", document.getElementById('password').value)
    const roleSelect = document.getElementById('roles');

    const selectedOptions = [];
    for (let i = 0; i < roleSelect.options.length; i++) {
        if (roleSelect.options[i].selected) {
            selectedOptions.push(roleSelect.options[i].value);
        }
    }
    formData.append('roles', selectedOptions);

    fetch("/users", {
        method: 'POST',  // Указываем метод POST
        body: formData, // Передаем данные формы
    })
        .then(async response => {
            if (response.ok) {
                await loadData()
                hideTab2AndSwitchToTab1()
                form.reset();

            } else {
                console.error('Error adding user:', response.status);
                response.text().then(errorMessage => {
                    console.error('Server error message:', errorMessage);
                });
            }
        })
        .catch(error => {
            alert(error.toString())
        });
}

async function fillRolesSelector() {
    try {
        const response = await fetch('/roles'); // Выполняем GET-запрос к /roles
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const roles = await response.json();
        const selectElement = document.getElementById('roles'); // Находим селектор по его ID
        selectElement.innerHTML = '';
        const selectElement1 = document.getElementById('roles1'); // Находим селектор по его ID
        selectElement1.innerHTML = '';

        roles.forEach(role => {
            const option = document.createElement('option');
            option.value = role.id;
            option.text = role.name;
            selectElement.appendChild(option);
            selectElement1.appendChild(option);
        });
    } catch (error) {
        console.error('Ошибка при получении или обработке списка ролей:', error);
    }
}


function hideTab2AndSwitchToTab1() {
    $('#tab2').hide(); // Скрываем содержимое второй вкладки (div с id="tab2")
    // $('#tab2-tab').hide(); // Скрываем кнопку второй вкладки (li с id="tab2-tab")
    $('#tab1-tab').tab('show'); // Переключаемся на первую вкладку
}
function loadUser(id){
    let getURL = 'http://localhost:8080/admin/edit/' + id;
    fetch(getURL)
        .then(response => {
            if (!response.ok) {
                throw new Error('Сетевая ошибка: ' + response.status);
            }
            return response.text();
        })
        .then(data => {
            const parser = new DOMParser();
            const doc = parser.parseFromString(data, 'text/html');
            const inputElement2 = doc.getElementById('firstName');
            const inputElement3 = doc.getElementById('lastName')
            const inputElement4 = doc.getElementById('age');
            const inputElement5 = doc.getElementById('username')
            const inputElement6 = doc.getElementById('password');
            document.getElementById('Id1').value = id;
            document.getElementById('firstName1').value = inputElement2.value;
            document.getElementById('lastName1').value = inputElement3.value;
            document.getElementById('age1').value = inputElement4.value;
            document.getElementById('username1').value = inputElement5.value;
            document.getElementById('password1').value = inputElement6.value;
        })
        .catch(error => {
            console.error('Ошибка при запросе:', error); // Обработка ошибок
        });
}
function collectingUserData(id){
    const formData = new FormData();
    formData.append('id', id);
    formData.append('username', document.getElementById('username1').value);
    formData.append('firstName', document.getElementById('firstName1').value);
    formData.append('lastName', document.getElementById('lastName1').value);
    formData.append('password', document.getElementById('password1').value);
    formData.append('age', document.getElementById('age1').value);
    const roleSelect = document.getElementById('roles1');

    const selectedOptions = [];
    for (let i = 0; i < roleSelect.options.length; i++) {
        if (roleSelect.options[i].selected) {
            selectedOptions.push(roleSelect.options[i].value);
        }
    }
    formData.append('roles', selectedOptions);
    return formData;
}



