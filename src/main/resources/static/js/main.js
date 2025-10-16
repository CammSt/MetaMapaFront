
// Objeto para manejar el estado de la sesión.
const AppState = {
    currentUser: JSON.parse(sessionStorage.getItem('currentUser')) || null
};

// Función para simular el inicio de sesión de diferentes roles.
/*function loginAs(role) {
    const user = role === 'contributor'
        ? { id: 1, name: 'Ana', lastName: 'Perez', role: 'contributor', birthDate: '1990-05-15', email: 'ana.perez@example.com' }
        : { id: 99, name: 'Admin', lastName: 'Principal', role: 'admin', birthDate: '1985-01-01', email: 'admin@example.com' };
    sessionStorage.setItem('currentUser', JSON.stringify(user));
    window.location.href = '/';
}*/

// Función para cerrar la sesión del usuario.
function logout() {
    sessionStorage.clear();
    window.location.href = '/';
}

function renderSidebar(currentPage) {
    const { currentUser } = AppState;
    let userMenu = '';

    if (currentUser) {
        userMenu = `<li class="nav-item mb-2"><a href="/profile" class="nav-link w-100 text-start py-2 rounded-3 ${currentPage === 'profile' ? 'active' : ''}"><i class="bi bi-person-circle me-2"></i> Mi Perfil</a></li>`;
    }

    let navLinks = `<li class="nav-item mb-2"><a href="/" class="nav-link w-100 text-start py-2 rounded-3 ${currentPage === 'index' ? 'active' : ''}"><i class="bi bi-grid me-2"></i> Colecciones</a></li>`;

    if (currentUser) {
        if (currentUser.role === 'admin') navLinks += `<li class="nav-item mb-2"><a href="/admin" class="nav-link w-100 text-start py-2 rounded-3 ${currentPage === 'admin' ? 'active' : ''}"><i class="bi bi-person-gear me-2"></i> Administración</a></li>`;
        if (currentUser.role === 'contributor') navLinks += `<li class="nav-item mb-2"><a href="/contributor" class="nav-link w-100 text-start py-2 rounded-3 ${currentPage === 'contributor' ? 'active' : ''}"><i class="bi bi-person-workspace me-2"></i> Mi Panel</a></li>`;
        navLinks += `${userMenu}<li class="nav-item mt-auto"><button id="logout-btn" class="nav-link w-100 text-start py-2 rounded-3 text-danger"><i class="bi bi-box-arrow-left me-2"></i> Cerrar Sesión</button></li>`;
    } else {
        navLinks += `<li class="nav-item mb-2"><a href="/login" class="nav-link w-100 text-start py-2 rounded-3 ${currentPage === 'login' ? 'active' : ''}"><i class="bi bi-box-arrow-in-right me-2"></i> Iniciar Sesión</a></li>`;
    }

    const termsLink = `<li class="nav-item"><a href="#" id="terms-link" class="nav-link w-100 text-start py-2 rounded-3"><i class="bi bi-file-text me-2"></i> Términos y Condiciones</a></li>`;

    const imagePath = "/assets/logo.png";
    const sidebar = document.getElementById('sidebar');
    sidebar.innerHTML = `<div class="d-flex flex-column h-100"><div class="sidebar-header d-flex align-items-center mb-4 pb-3 border-bottom"><img src="${imagePath}" alt="MetaMapa Logo" class="logo-sidebar" style="border-radius: 100%;"><h2 class="display-9 fw-bold text-primary mb-0">MetaMapa</h2></div><ul class="nav nav-pills flex-column flex-grow-1">${navLinks}</ul><div class="border-top pt-3 mt-auto">${termsLink}<small class="text-muted d-block mt-2">DSI - 2025 (mi-no-grupo-24)</small></div></div>`;

    const logoutBtn = document.getElementById('logout-btn');
    if (logoutBtn) logoutBtn.addEventListener('click', logout);
    document.getElementById('terms-link').addEventListener('click', (e) => {
        e.preventDefault();
        openModal(renderTermsModal);
    });
}
// --- FUNCIONES PARA RENDERIZAR MODALES (RESTAURADAS Y COMPLETAS) ---

function openModal(modalRenderFunc, ...args) {
    const modalContainer = document.getElementById('modal-container');
    modalContainer.innerHTML = '';
    modalRenderFunc(modalContainer, ...args);
    const modalElement = modalContainer.querySelector('.modal');
    if (modalElement) {
        const modal = new bootstrap.Modal(modalElement);
        modal.show();
        modalElement.addEventListener('hidden.bs.modal', () => { modalContainer.innerHTML = ''; }, { once: true });
        return modalElement; // Devuelve el elemento para añadirle eventos después
    }
    return null;
}

// Modal de Términos y Condiciones
function renderTermsModal(container) {
    container.innerHTML = `
        <div class="modal fade" id="terms-modal" tabindex="-1" aria-labelledby="termsModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable modal-lg">
                <div class="modal-content rounded-4 shadow-lg border-0">
                    <div class="modal-header bg-primary text-white">
                        <h5 class="modal-title fw-bold" id="termsModalLabel">Términos y Condiciones de Uso</h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body p-4">
                        <p class="fw-bold">1. Aceptación de los Términos</p>
                        <p class="text-muted">Al acceder y utilizar la plataforma MetaMapa (en adelante, "la Plataforma"), usted acepta y acuerda estar sujeto a los siguientes términos y condiciones. Si no está de acuerdo con alguna parte de los términos, no podrá utilizar nuestros servicios.</p>

                        <p class="fw-bold mt-4">2. Uso de la Plataforma</p>
                        <p class="text-muted">Usted se compromete a utilizar la Plataforma de manera responsable y con fines lícitos. Queda prohibido subir contenido que sea falso, difamatorio, ilegal, que incite al odio o que viole los derechos de terceros.</p>

                        <p class="fw-bold mt-4">3. Contenido del Usuario</p>
                        <p class="text-muted">Al subir un "Hecho" o cualquier otro contenido, usted otorga a la Plataforma una licencia no exclusiva, mundial y libre de regalías para usar, reproducir y mostrar dicho contenido. Usted declara ser el propietario de los derechos del contenido que aporta o tener los permisos necesarios para ello.</p>

                        <p class="fw-bold mt-4">4. Proceso de Verificación</p>
                        <p class="text-muted">Todo el contenido sugerido por los contribuyentes está sujeto a un proceso de revisión y verificación por parte de los administradores de la Plataforma. MetaMapa se reserva el derecho de aceptar, rechazar o eliminar cualquier contenido a su entera discreción sin previo aviso.</p>
                        
                        <p class="fw-bold mt-4">5. Limitación de Responsabilidad</p>
                        <p class="text-muted">La información presentada en la Plataforma se proporciona "tal cual" y se basa en las contribuciones de la comunidad. MetaMapa no garantiza la exactitud, integridad o actualidad de la información y no se hace responsable de las decisiones tomadas con base en el contenido del sitio.</p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-primary rounded-pill px-4" data-bs-dismiss="modal">Entendido</button>
                    </div>
                </div>
            </div>
        </div>`;
}

// Modal para Subir/Editar Hecho (con todos los campos)
function renderFactModal(container, fact = null) {
    const isEditing = fact !== null;
    // Estas categorías vendrían del backend, pero las simulamos aquí
    const availableCategories = ['Crimen de Odio', 'Incendio Forestal', 'Accidente Vial', 'Otro'];
    const categoryOptions = availableCategories.map(cat => `<option value="${cat}" ${fact?.categoria === cat ? 'selected' : ''}>${cat}</option>`).join('');

    container.innerHTML = `
        <div class="modal fade" id="fact-modal" tabindex="-1">
            <div class="modal-dialog modal-lg modal-dialog-centered">
                <div class="modal-content rounded-4 shadow-lg border-0">
                    <form>
                        <div class="modal-header">
                            <h5 class="modal-title text-primary fw-bold">${isEditing ? 'Editar' : 'Subir'} Hecho</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <p class="text-muted small">${isEditing ? 'Modifica los datos de tu hecho.' : 'Tu sugerencia será revisada por un administrador.'}</p>
                            <div class="mb-3"><label class="form-label">Título</label><input type="text" class="form-control" value="${fact?.titulo || ''}" required></div>
                            <div class="mb-3"><label class="form-label">Descripción</label><textarea class="form-control" rows="3" required>${fact?.descripcion || ''}</textarea></div>
                            <div class="row">
                                <div class="col-md-6 mb-3"><label class="form-label">Categoría</label><select class="form-select">${categoryOptions}</select></div>
                                <div class="col-md-6 mb-3"><label class="form-label">Fecha</label><input type="date" value="${fact?.fechaAcontecimiento ? new Date(fact.fechaAcontecimiento).toISOString().split('T')[0] : ''}" class="form-control"></div>
                            </div>
                            <div class="mb-3"><label class="form-label">Archivo Multimedia</label><input class="form-control" type="file" accept="image/*,video/*"></div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                            <button type="submit" class="btn btn-primary">${isEditing ? 'Guardar Cambios' : 'Enviar Sugerencia'}</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>`;
}

// Modal para Crear/Editar Colección (con todos los campos)
function renderCollectionModal(container, collection = null, consensusLabels = {}, availableSources = []) {
    const isEditing = collection !== null;

    // Convertimos los consensusLabels a options para el select
    const consensusOptions = Object.entries(consensusLabels)
        .map(([value, label]) => `<option value="${value.toUpperCase()}" ${collection?.algoritmoConsenso === value.toUpperCase() ? 'selected' : ''}>${label}</option>`)
        .join('');

    container.innerHTML = `
        <div class="modal fade" id="collection-modal">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <form>
                        <div class="modal-header"><h5>${isEditing ? 'Editar' : 'Crear'} Colección</h5><button type="button" class="btn-close" data-bs-dismiss="modal"></button></div>
                        <div class="modal-body">
                            <input type="hidden" name="handleID" value="${collection?.handleID || ''}">
                            
                            <div class="mb-3"><label class="form-label">Título</label><input type="text" name="titulo" class="form-control" value="${collection?.titulo || ''}" required></div>
                            <div class="mb-3"><label class="form-label">Descripción</label><textarea name="descripcion" class="form-control" rows="3" required>${collection?.descripcion || ''}</textarea></div>
                            <div class="mb-4"><label class="form-label">Algoritmo de Consenso</label><select name="algoritmoConsenso" class="form-select">${consensusOptions}</select></div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                            <button type="submit" class="btn btn-primary">Guardar</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>`;
}

// Modal para Evaluar Solicitud (con botones)
function renderRequestReviewModal(container, request) {
    container.innerHTML = `
        <div class="modal fade" id="req-review-modal">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title text-primary fw-bold">Evaluar Solicitud</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <h6>Solicitud #${request.nroDeSolicitud}: "${request.tituloDelHechoAEliminar}"</h6>
                        <p><strong>Estado:</strong> ${request.estado}</p>
                        <p><strong>Motivo:</strong><br>${request.motivo || 'No especificado.'}</p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" id="btn-reject-request" data-request-id="${request.nroDeSolicitud}" class="btn btn-danger">Rechazar</button>
                        <button type="button" id="btn-approve-request" data-request-id="${request.nroDeSolicitud}" class="btn btn-success">Aprobar</button>
                    </div>
                </div>
            </div>
        </div>`;
}

// Modal para Importar CSV
function renderCsvImportModal(container) {
    container.innerHTML = `
        <div class="modal fade" id="csv-import-modal" tabindex="-1">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <form>
                        <div class="modal-header">
                            <h5 class="modal-title text-primary fw-bold">Importar desde CSV</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <p class="text-muted small">Selecciona un archivo .csv para la carga masiva de hechos.</p>
                            <div class="mb-3">
                                <label for="csvFile" class="form-label">Archivo CSV</label>
                                <input class="form-control" type="file" id="csvFile" accept=".csv" required>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                            <button type="submit" class="btn btn-primary">Importar</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>`;
}

function renderFactDetailModal(container, fact) {
    const formattedDate = new Date(fact.fechaAcontecimiento).toLocaleDateString('es-ES', { year: 'numeric', month: 'long', day: 'numeric' });
    container.innerHTML = `
        <div class="modal fade" id="fact-detail-modal" tabindex="-1">
            
            <div class="modal-dialog modal-lg modal-dialog-centered"> 
                <div class="modal-content rounded-4 shadow-lg border-0">
                    <div class="modal-header bg-light border-bottom-0">
                        <div>
                            <h5 class="modal-title text-primary fw-bold">${fact.titulo}</h5>
                            <span class="badge bg-secondary">${fact.categoria}</span>
                        </div>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body p-4">

                        <p class="text-muted" style="overflow-wrap: break-word;">${fact.descripcion}</p>

                        <hr>
                        <div class="row">
                            <div class="col-md-6"><h6 class="fw-bold">Detalles</h6><ul class="list-unstyled"><li><strong><i class="bi bi-calendar-event me-2"></i>Fecha:</strong> ${formattedDate}</li></ul></div>
                            <div class="col-md-6"><h6 class="fw-bold">Ubicación</h6><div id="mini-map" style="height: 150px; width: 100%; border-radius: 8px; background-color: #eee;"></div></div>
                        </div>
                    </div>
                    <div class="modal-footer"><button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cerrar</button></div>
                </div>
            </div>
        </div>`;
}


// Maneja el envío del formulario de Colecciones (Crear/Editar).
// Se comunica directamente con la API del backend.

async function handleCollectionFormSubmit(form, modal) {
    const id = form.querySelector('input[name="handleID"]').value;
    const isEditing = !!id;

    const url = isEditing ? `/colecciones/${id}` : '/colecciones';
    const method = isEditing ? 'PUT' : 'POST';

    // Construimos el objeto DTO que espera el backend
    const coleccionData = {
        titulo: form.querySelector('input[name="titulo"]').value,
        descripcion: form.querySelector('textarea[name="descripcion"]').value,
        algoritmoConsenso: form.querySelector('select[name="algoritmoConsenso"]').value,
        visualizadorID: AppState.currentUser ? AppState.currentUser.name : null // Asegúrate que 'name' sea el ID
    };

    try {
        const response = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(coleccionData)
        });

        if (response.ok) {
            modal.hide();
            location.reload();
        } else {
            const errorData = await response.json();
            alert('Error al guardar: ' + (errorData.message || 'Por favor, revise los campos.'));
        }
    } catch (error) {
        console.error('Error de conexión:', error);
        alert('Error de conexión con el servidor. Inténtelo más tarde.');
    }
}


// Maneja la eliminación de una colección.

async function handleDeleteCollection(collectionId) {
    if (!confirm('¿Estás seguro de que deseas eliminar esta colección?')) {
        return;
    }

    const visualizadorID = AppState.currentUser ? AppState.currentUser.name : null;
    if (!visualizadorID) {
        alert('Debes estar logueado para realizar esta acción.');
        return;
    }

    const url = `/colecciones/${collectionId}?visualizadorID=${visualizadorID}`;

    try {
        const response = await fetch(url, { method: 'DELETE' });
        if (response.ok) {
            location.reload();
        } else {
            alert('Error al eliminar la colección.');
        }
    } catch (error) {
        console.error('Error de conexión:', error);
        alert('Error de conexión con el servidor.');
    }
}


// Maneja la decisión de aprobar o rechazar una solicitud.

async function handleReviewRequest(requestId, isApproved) {
    const visualizadorID = AppState.currentUser ? AppState.currentUser.name : null;
    if (!visualizadorID) {
        alert('Debes estar logueado para realizar esta acción.');
        return;
    }

    // La URL apunta al endpoint PUT del backend
    const url = `/solicitudes/${requestId}?visualizadorId=${visualizadorID}&aceptado=${isApproved}`;

    try {
        const response = await fetch(url, { method: 'PUT' });
        if (response.ok) {
            location.reload();
        } else {
            const errorData = await response.json();
            alert('Error al procesar la solicitud: ' + (errorData.message || 'Error desconocido.'));
        }
    } catch (error) {
        console.error('Error de conexión:', error);
        alert('Error de conexión con el servidor.');
    }
}

function renderFactList(facts) {
    factsListContainer.innerHTML = '';
    if (facts.length === 0) {
        factsListContainer.innerHTML = '<div class="col-12"><div class="card card-body text-center text-muted">No hay hechos que coincidan con los filtros.</div></div>';
        return;
    }
    facts.forEach(fact => {
        const factCard = `
        <div class="col-md-6 col-lg-4">
            <div data-fact-id="${fact.id}" class="card h-100 shadow-sm border-0 rounded-3 custom-card-hover fact-card" style="cursor: pointer;">
                <div class="card-body d-flex flex-column">
                    <div class="d-flex justify-content-between mb-2">
                        <h6 class="card-title text-primary fw-bold">${fact.titulo}</h6>
                        <div class="dropdown">
                            <button class="btn btn-sm btn-light py-0 px-2" type="button" data-bs-toggle="dropdown" onclick="event.stopPropagation();">•••</button>
                            <ul class="dropdown-menu dropdown-menu-end">
                                <li><a class="dropdown-item btn-request-deletion" href="#" data-fact-title="${fact.titulo}">Solicitar Eliminación</a></li>
                            </ul>
                        </div>
                    </div>
                    <p class="card-text text-muted small flex-grow-1">${fact.descripcion}</p>
                    <div class="mt-auto pt-3 border-top">
                        <span class="badge bg-secondary me-2">${fact.categoria}</span>
                        <small class="text-muted">Fecha: ${new Date(fact.fechaAcontecimiento).toLocaleDateString('es-ES')}</small>
                    </div>
                </div>
            </div>
        </div>`;
        factsListContainer.innerHTML += factCard;
    });
    addEventListenersToCards();

    // 👇 Es importante llamar a la función que agrega los eventos a los nuevos botones
    addRequestDeletionEventListeners();
}

//Renderiza el modal para crear una nueva solicitud de eliminación.

function renderDeletionRequestModal(container, factTitle) {
    container.innerHTML = `
        <div class="modal fade" id="deletion-request-modal">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <form>
                        <div class="modal-header">
                            <h5 class="modal-title text-primary fw-bold">Solicitar Eliminación</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <p>Estás solicitando la eliminación del hecho: <strong>"${factTitle}"</strong></p>
                            <input type="hidden" name="tituloHecho" value="${factTitle}">
                            
                            <div class="mb-3">
                                <label for="motivo" class="form-label">Motivo de la solicitud</label>
                                <textarea name="motivo" class="form-control" rows="6" required minlength="500" placeholder="Por favor, describe detalladamente por qué este hecho debería ser eliminado (mínimo 500 caracteres)."></textarea>
                                <div class="form-text">Tu solicitud será revisada por un administrador.</div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                            <button type="submit" class="btn btn-danger">Enviar Solicitud</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>`;
}

// Maneja el envío del formulario de solicitud de eliminación.
// Se comunica con la API del backend.

async function handleDeletionRequestSubmit(form, modal) {
    const requestData = {
        tituloHecho: form.querySelector('input[name="tituloHecho"]').value,
        motivo: form.querySelector('textarea[name="motivo"]').value
    };

    // Validamos en el frontend para dar feedback rápido
    if (requestData.motivo.length < 500) {
        alert('El motivo debe tener al menos 500 caracteres.');
        return;
    }

    try {
        const response = await fetch('/solicitudes', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(requestData)
        });

        if (response.ok) {
            modal.hide();
            alert('¡Solicitud enviada exitosamente!');
            window.location.href = '/contributor';
        } else {
            const errorData = await response.json();
            alert('Error al enviar la solicitud: ' + (errorData.message || 'Error desconocido.'));
        }
    } catch (error) {
        console.error('Error de conexión:', error);
        alert('Error de conexión con el servidor.');
    }
}

// --- INICIALIZACIÓN ---
document.addEventListener('DOMContentLoaded', () => {
    const toggler = document.getElementById('sidebar-toggler-btn');
    const sidebar = document.getElementById('sidebar');
    if (toggler) toggler.addEventListener('click', () => sidebar.classList.toggle('show'));
});