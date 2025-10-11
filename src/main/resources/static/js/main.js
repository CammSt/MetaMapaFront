
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
    const consensusOptions = Object.entries(consensusLabels).map(([value, label]) => `<option value="${value}" ${collection?.consensus_algorithm === value ? 'selected' : ''}>${label}</option>`).join('');
    const sourceOptions = availableSources.map(source => `<option value="${source}" ${collection?.source === source ? 'selected' : ''}>${source}</option>`).join('');

    container.innerHTML = `
        <div class="modal fade" id="collection-modal" tabindex="-1">
            <div class="modal-dialog modal-lg modal-dialog-centered">
                <div class="modal-content rounded-4 shadow-lg border-0">
                    <form>
                        <div class="modal-header border-bottom-0 pb-0">
                            <h5 class="modal-title text-primary fw-bold">${isEditing ? 'Editar' : 'Crear'} Colección</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body p-4 pt-0">
                            <div class="mb-3"><label class="form-label fw-semibold">Título</label><input type="text" name="title" class="form-control" value="${collection?.titulo || ''}" required></div>
                            <div class="mb-3"><label class="form-label fw-semibold">Descripción</label><textarea name="description" class="form-control" rows="3" required>${collection?.descripcion || ''}</textarea></div>
                            <div class="mb-3"><label class="form-label fw-semibold">Fuente</label><select name="source" class="form-select">${sourceOptions}</select></div>
                            <div class="mb-4"><label class="form-label fw-semibold">Algoritmo de Consenso</label><select name="consensus_algorithm" class="form-select">${consensusOptions}</select></div>
                        </div>
                        <div class="modal-footer pt-3 border-top">
                            <button type="button" class="btn btn-secondary rounded-pill px-4" data-bs-dismiss="modal">Cancelar</button>
                            <button type="submit" class="btn btn-primary rounded-pill px-4">Guardar</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>`;
}

// Modal para Evaluar Solicitud (con botones)
function renderRequestReviewModal(container, request) {
    container.innerHTML = `
        <div class="modal fade" id="req-review-modal" tabindex="-1">
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
                        <button type="button" class="btn btn-danger" data-bs-dismiss="modal">Rechazar</button>
                        <button type="button" class="btn btn-success" data-bs-dismiss="modal">Aprobar</button>
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

// --- INICIALIZACIÓN ---
document.addEventListener('DOMContentLoaded', () => {
    const toggler = document.getElementById('sidebar-toggler-btn');
    const sidebar = document.getElementById('sidebar');
    if (toggler) toggler.addEventListener('click', () => sidebar.classList.toggle('show'));
});