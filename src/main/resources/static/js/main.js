// Objeto para manejar el estado de la sesión.
const AppState = {
    currentUser: JSON.parse(sessionStorage.getItem('userJson')) || null
};

// Función para cerrar la sesión del usuario.
function logout() {
    sessionStorage.clear();
    window.location.href = '/logout';
}

function renderSidebar(currentPage) {
    const { currentUser } = AppState;
    let userMenu = '';

    if (currentUser) {
        userMenu = `<li class="nav-item mb-2"><a href="/profile" class="nav-link w-100 text-start py-2 rounded-3 ${currentPage === 'profile' ? 'active' : ''}"><i class="bi bi-person-circle me-2"></i> Mi Perfil</a></li>`;
    }

    let navLinks = `<li class="nav-item mb-2"><a href="/" class="nav-link w-100 text-start py-2 rounded-3 ${currentPage === 'index' ? 'active' : ''}"><i class="bi bi-grid me-2"></i> Colecciones</a></li>`;

    if (currentUser) {
        if (currentUser.role === 'admin') {
            navLinks += `<li class="nav-item mb-2"><a href="/admin" class="nav-link w-100 text-start py-2 rounded-3 ${currentPage === 'admin' ? 'active' : ''}"><i class="bi bi-person-gear me-2"></i> Administración</a></li>`;
            navLinks += `<li class="nav-item mb-2"><a href="/contributor" class="nav-link w-100 text-start py-2 rounded-3 ${currentPage === 'contributor' ? 'active' : ''}"><i class="bi bi-person-workspace me-2"></i> Mis Contribuciones </a></li>`;
        } else if (currentUser.role === 'contributor') {
            navLinks += `<li class="nav-item mb-2"><a href="/contributor" class="nav-link w-100 text-start py-2 rounded-3 ${currentPage === 'contributor' ? 'active' : ''}"><i class="bi bi-person-workspace me-2"></i> Mi Panel</a></li>`;
        }

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
// --- FUNCIONES PARA RENDERIZAR MODALES  ---

// ... dentro de main.js ...

function openModal(modalRenderFunc, ...args) {
    const modalContainer = document.getElementById('modal-container');
    modalContainer.innerHTML = '';
    modalRenderFunc(modalContainer, ...args);
    const modalElement = modalContainer.querySelector('.modal');
    if (modalElement) {
        const modal = new bootstrap.Modal(modalElement);
        modal.show();
        modalElement.addEventListener('hidden.bs.modal', () => { modalContainer.innerHTML = ''; }, { once: true });

        // --- CAMBIO AQUÍ: Devolver el elemento DOM, no la instancia ---
        return modalElement;
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

function renderFactDetailModal(container, fact) {
    const formattedDate = new Date(fact.fechaAcontecimiento).toLocaleDateString('es-ES', { year: 'numeric', month: 'long', day: 'numeric' });
    const fuenteTexto = fact.nombreOrigen || 'No especificada';

    // --- LÓGICA DE MULTIMEDIA ---
    // --- LÓGICA DE MULTIMEDIA CORREGIDA ---
    let imageHtml = '';
    if (fact.contenidoMultimedia) {
        let imageUrl = fact.contenidoMultimedia;

        // Aseguramos que siempre apunte al backend de Fuente Dinámica (puerto 8082)
        // Si la URL no incluye el dominio correcto, se lo forzamos.
        const baseUrlBackend = 'http://localhost:8082';

        if (!imageUrl.includes(baseUrlBackend)) {
            // Quitamos cualquier barra inicial o 'http://...' incorrecto que pueda tener
            imageUrl = imageUrl.replace(/^(https?:\/\/[^\/]+)?\//, '');
            // Construimos la URL final correcta
            imageUrl = `${baseUrlBackend}/uploads/${imageUrl}`;
        }

        imageHtml = `
        <div class="mb-4 text-center bg-light p-2 rounded border">
            <img src="${imageUrl}" 
                 class="img-fluid rounded shadow-sm" 
                 style="max-height: 350px; width: auto;" 
                 alt="Evidencia del hecho"
                 onerror="this.style.display='none'; this.nextElementSibling.style.display='block';">
            <div class="text-muted small mt-2" style="display:none;">
                <i class="bi bi-exclamation-triangle me-1"></i> No se pudo cargar la imagen.
            </div>
        </div>`;
    }
// ---------------------------------------

    container.innerHTML = `
        <div class="modal fade" id="fact-detail-modal" tabindex="-1">
            <div class="modal-dialog modal-lg modal-dialog-centered modal-dialog-scrollable"> 
                <div class="modal-content rounded-4 shadow-lg border-0">
                    <div class="modal-header bg-light border-bottom-0">
                        <div>
                            <h5 class="modal-title text-primary fw-bold">${fact.titulo}</h5>
                            <span class="badge bg-secondary">${fact.categoria}</span>
                        </div>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body p-4">
                        

                        <h6 class="fw-bold text-dark border-bottom pb-2 mb-3">Descripción</h6>
                        <p class="text-muted" style="white-space: pre-wrap; overflow-wrap: break-word;">${fact.descripcion}</p>

                        <div class="row mt-4 pt-3 border-top">
                            <div class="col-md-6">
                                <h6 class="fw-bold mb-3">Información Adicional</h6>
                                <ul class="list-unstyled text-muted small">
                                    <li class="mb-2"><strong><i class="bi bi-calendar-event me-2 text-primary"></i>Fecha:</strong> ${formattedDate}</li>
                                    <li class="mb-2"><strong><i class="bi bi-hdd-network me-2 text-primary"></i>Fuente:</strong> ${fuenteTexto}</li>
                                    <li><strong><i class="bi bi-person-check me-2 text-primary"></i>Consenso:</strong> ${fact.consensuado ? '<span class="text-success">Verificado</span>' : '<span class="text-warning">En revisión</span>'}</li>
                                </ul>
                            </div>
                            <div class="col-md-6">
                                <h6 class="fw-bold mb-2">Ubicación</h6>
                                <div id="mini-map" style="height: 180px; width: 100%; border-radius: 8px; background-color: #eee; border: 1px solid #ddd;"></div>
                            </div>
                        </div>
                        <div class="row mt-4 pt-3 border-top">
                            ${imageHtml}
                        </div>                     
                    </div>
                    <div class="modal-footer bg-light">
                        <button type="button" class="btn btn-outline-secondary rounded-pill px-4" data-bs-dismiss="modal">Cerrar</button>
                    </div>
                </div>
            </div>
        </div>`;
}

// =========================================================================================
// FUNCIÓN renderRequestDetailModal (Crítica para el Admin/Contributor)
// =========================================================================================

function renderRequestDetailModal(container, requestData) {

    const requestType = requestData.tipo || (requestData.idHechoOriginal ? 'Edición' : (requestData.motivo ? 'Eliminación' : 'Nuevo Hecho'));
    const requestEstado = requestData.estado || 'N/A';
    const isEdicion = requestType === 'Edición';
    const isEliminacion = requestType === 'Eliminación';

    // Campos primarios
    const titulo = requestData.titulo || requestData.tituloPropuesto || requestData.tituloDelHechoAEliminar || requestData.nombreHecho || `Hecho ID ${requestData.idHechoOriginal || requestData.id}`;
    const descripcionPropuesta = requestData.descripcionPropuesta || requestData.descripcion || '';
    const motivoEliminacion = requestData.motivo || '';

    // --- LÓGICA DE FEEDBACK DEL ADMIN (Sugerencias / Rechazo) ---
    let adminFeedbackHtml = '';

    // Recuperamos el mensaje, priorizando 'sugerenciaAdmin' que viene del backend dinámico
    const mensajeAdmin = requestData.sugerenciaAdmin || requestData.detalle || '';

    if (requestEstado === 'ACEPTADO_CON_SUGERENCIAS') {
        adminFeedbackHtml = `
            <div class="alert alert-warning border-warning shadow-sm mb-3">
                <div class="d-flex">
                    <i class="bi bi-lightbulb-fill fs-4 me-3"></i>
                    <div>
                        <h6 class="alert-heading fw-bold mb-1">Sugerencia del Administrador:</h6>
                        <p class="mb-0 text-dark">${mensajeAdmin || 'Sin detalles adicionales.'}</p>
                    </div>
                </div>
            </div>`;
    } else if (requestEstado === 'RECHAZADO' && mensajeAdmin) {
        adminFeedbackHtml = `
            <div class="alert alert-danger border-danger shadow-sm mb-3">
                <h6 class="alert-heading fw-bold"><i class="bi bi-x-circle me-2"></i>Motivo del Rechazo:</h6>
                <p class="mb-0">${mensajeAdmin}</p>
            </div>`;
    }
    // ------------------------------------------------------------

    // Campos técnicos y ubicación
    const lat = requestData.latitud || requestData.latitudPropuesta;
    const lon = requestData.longitud || requestData.longitudPropuesta;
    const fechaAcontecimiento = requestData.fechaAcontecimiento || requestData.fechaAcontecimientoPropuesta;
    const categoriaNombre = requestData.categoria || (requestData.categoriaPropuesta && requestData.categoriaPropuesta.nombre) || 'No especificada';
    const contenidoMultimedia = requestData.contenidoMultimediaPropuesto || requestData.contenidoMultimedia;

    // --- Contenido Principal ---
    let propuestaContent = '';

    if (isEliminacion) {
        propuestaContent += `<p class="fw-bold text-danger">Motivo de Eliminación:</p><p class="text-muted" style="white-space: pre-wrap;">${motivoEliminacion || 'No especificado.'}</p>`;
        propuestaContent += `<p><strong>Título del Hecho a Eliminar:</strong> ${requestData.nombreHecho || titulo}</p>`;
    } else {
        propuestaContent += `<p class="fw-bold text-primary">${isEdicion ? 'Detalles de la Edición Propuesta' : 'Detalles del Hecho'}</p>`;
        propuestaContent += `<p><strong>Descripción:</strong> <span class="text-muted" style="white-space: pre-wrap;">${descripcionPropuesta || 'Sin descripción.'}</span></p>`;
        propuestaContent += `<hr><h6 class="fw-bold">Datos Técnicos:</h6>`;

        let detallesTecnicos = `<ul class="list-unstyled small">`;
        detallesTecnicos += `<li><strong>Título:</strong> ${titulo}</li>`;
        detallesTecnicos += `<li><strong>Categoría:</strong> <span class="badge bg-secondary">${categoriaNombre}</span></li>`;

        if (lat && lon) {
            detallesTecnicos += `<li><strong>Ubicación:</strong> Latitud: ${lat} | Longitud: ${lon}</li>`;
            detallesTecnicos += `<div id="request-map-container" data-lat="${lat}" data-lon="${lon}" style="height: 200px; width: 100%; margin-top: 10px; border-radius: 8px; border: 1px solid #ddd; z-index: 1;"></div>`;
        } else {
            detallesTecnicos += `<li><strong>Ubicación:</strong> No especificada</li>`;
        }

        if (fechaAcontecimiento) {
            const formattedDate = new Date(fechaAcontecimiento).toLocaleString('es-ES');
            detallesTecnicos += `<li><strong>Fecha del Acontecimiento:</strong> ${formattedDate}</li>`;
        }
        if (contenidoMultimedia) {
            detallesTecnicos += `<li><strong>Contenido Multimedia:</strong>`;
            detallesTecnicos += `
                <div class="mt-2 p-2 border rounded bg-white text-center">
                    <img src="${contenidoMultimedia}" 
                         class="img-fluid rounded shadow-sm" 
                         style="max-height: 300px; max-width: 100%;" 
                         alt="Evidencia adjunta"
                         onerror="this.style.display='none'; this.insertAdjacentHTML('afterend', '<div class=\'text-muted small\'><i class=\'bi bi-file-earmark\'></i> ${contenidoMultimedia} (No es una imagen previsualizable)</div>');">
                </div>`;
            detallesTecnicos += `</li>`;
        } else {
            detallesTecnicos += `<li><strong>Contenido Multimedia:</strong> No se adjuntó archivo.</li>`;
        }

        if (requestData.idHechoOriginal) {
            detallesTecnicos += `<li><strong>ID Hecho Original:</strong> ${requestData.idHechoOriginal}</li>`;
        }
        detallesTecnicos += `</ul>`;

        propuestaContent += detallesTecnicos;
    }

    // --- Estilos y Elementos de Estado ---
    const statusClass = requestEstado === 'PENDIENTE'
        ? 'bg-warning'
        : (requestEstado.includes('APROBA') || requestEstado.includes('ACEPTA') ? 'bg-success' : 'bg-danger');

    const modalTitleText = (requestEstado === 'PENDIENTE' ? `Revisar Solicitud de ${requestType}` : `Detalle de Resolución de ${requestType}`);

    // --- Construcción del HTML Final ---
    const modalBodyHtml = `
        <div id="request-modal-body">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h6 class="fw-bold mb-0">Tipo: <span class="badge bg-primary">${requestType}</span></h6>
                <h6 class="fw-bold mb-0">Estado: <span class="badge ${statusClass}">${requestEstado}</span></h6>
            </div>

            ${adminFeedbackHtml}

            <div class="card card-body bg-light mb-3 border-0 shadow-sm">
                ${propuestaContent}
            </div>
            
            <small class="text-muted d-block mt-3 text-end">ID Solicitud: ${requestData.id} | Solicitante: ${requestData.solicitanteId || requestData.visualizadorEditor || 'N/A'}</small>
        </div>
    `;

    container.innerHTML = `
        <div class="modal fade" id="request-detail-modal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-xl modal-dialog-scrollable modal-dialog-centered">
                <div class="modal-content rounded-4 shadow-lg border-0">
                    <div class="modal-header bg-primary text-white">
                        <h5 class="modal-title fw-bold">${modalTitleText}</h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss=\"modal\"></button>
                    </div>
                    <div class="modal-body p-4">
                        ${modalBodyHtml}
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary rounded-pill px-4" data-bs-dismiss="modal">Cerrar</button>
                    </div>
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