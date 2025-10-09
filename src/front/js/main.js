// js/main.js

const AppState = { currentUser: JSON.parse(sessionStorage.getItem('currentUser')) || null };

const mainContent = document.getElementById('main-content');
const modalContainer = document.getElementById('modal-container');
const sidebar = document.getElementById('sidebar');

function loginAs(role) {
    const user = role === 'contributor' 
        ? { id: 1, name: 'Ana', lastName: 'Perez', role: 'contributor', birthDate: '1990-05-15', email: 'ana.perez@example.com' } 
        : { id: 99, name: 'Admin', lastName: 'Principal', role: 'admin', birthDate: '1985-01-01', email: 'admin@example.com' };
    sessionStorage.setItem('currentUser', JSON.stringify(user));
    window.location.href = 'index.html';
}
function logout() {
    sessionStorage.clear();
    window.location.href = 'index.html';
}

function renderSidebar(currentPage) {
    const { currentUser } = AppState;
    let userMenu = '';
    if (currentUser) {
        userMenu = `
            <li class="nav-item mb-2">
                <a href="profile.html" class="nav-link w-100 text-start py-2 rounded-3 ${currentPage === 'profile' ? 'active bg-primary text-white' : ''}">
                    <i class="bi bi-person-circle me-2"></i> Mi Perfil
                </a>
            </li>
        `;
    }

    let navLinks = `
        <li class="nav-item mb-2"><a href="index.html" class="nav-link w-100 text-start py-2 rounded-3 ${currentPage === 'index' ? 'active bg-primary text-white' : ''}"><i class="bi bi-grid me-2"></i> Colecciones</a></li>`;
    if (currentUser) {
        if (currentUser.role === 'admin') navLinks += `<li class="nav-item mb-2"><a href="admin.html" class="nav-link w-100 text-start py-2 rounded-3 ${currentPage === 'admin' ? 'active bg-primary text-white' : ''}"><i class="bi bi-person-gear me-2"></i> Administración</a></li>`;
        if (currentUser.role === 'contributor') navLinks += `<li class="nav-item mb-2"><a href="contributor.html" class="nav-link w-100 text-start py-2 rounded-3 ${currentPage === 'contributor' ? 'active bg-primary text-white' : ''}"><i class="bi bi-person-workspace me-2"></i> Mi Panel</a></li>`;
        navLinks += `${userMenu}<li class="nav-item mt-auto"><button id="logout-btn" class="nav-link w-100 text-start py-2 rounded-3 text-danger"><i class="bi bi-box-arrow-left me-2"></i> Cerrar Sesión</button></li>`;
    } else {
        navLinks += `<li class="nav-item mb-2"><a href="login.html" class="nav-link w-100 text-start py-2 rounded-3 ${currentPage === 'login' ? 'active bg-primary text-white' : ''}"><i class="bi bi-box-arrow-in-right me-2"></i> Iniciar Sesión</a></li>`;
    }
    sidebar.innerHTML = `<div class="d-flex flex-column h-100"><div class="sidebar-header d-flex align-items-center mb-4 pb-3 border-bottom"><img src="https://via.placeholder.com/50" alt="MetaMapa Logo" class="logo-sidebar" style="border-radius: 100%;"><h2 class="display-9 fw-bold text-primary mb-0">MetaMapa</h2></div><ul class="nav nav-pills flex-column flex-grow-1">${navLinks}</ul><div class="border-top pt-3 mt-auto"><small class="text-muted">DSI - 2025 (mi-no-grupo-24)</small></div></div>`;
    const logoutBtn = document.getElementById('logout-btn');
    if (logoutBtn) logoutBtn.addEventListener('click', logout);
}

function openModal(modalRenderFunc, ...args) {
    modalRenderFunc(...args);
    const modalElement = modalContainer.querySelector('.modal');
    if (modalElement) {
        const modal = new bootstrap.Modal(modalElement);
        modal.show();
        modalElement.addEventListener('hidden.bs.modal', () => { modalContainer.innerHTML = ''; }, { once: true });
    }
}

function renderCollectionView() {
    mainContent.innerHTML = `<div class="p-4"><div class="text-center mb-5 animate__animated animate__fadeInDown"><h2 class="display-4 fw-bold text-primary">Información Colaborativa para un Mundo Transparente</h2><p class="lead text-secondary">MetaMapa es una plataforma abierta y ciudadana para mapear, verificar y visualizar información geolocalizada. Nuestro objetivo es construir una fuente de datos confiable sobre temas de interés público, impulsada por la comunidad.</p><button class="btn btn-primary rounded-pill px-4 py-2 fw-bold" data-create-fact>+ Subir Hecho</button></div><section class="animate__animated animate__fadeIn"><h3 class="mb-4 text-primary fw-bold border-bottom pb-2">Explora Nuestras Colecciones</h3><div class="row g-4">${collections.map(c=>`<div class="col-md-6 col-lg-4"><div class="card h-100 shadow-sm border-0 rounded-3 custom-card-hover"><div class="card-body d-flex flex-column"><h5 class="card-title text-primary fw-bold mb-2">${c.title}</h5><p class="card-text text-muted flex-grow-1 mb-3">${c.description}</p><div class="mt-auto pt-3 border-top d-grid gap-2"><a href="facts.html?collection_id=${c.handle_id}" class="btn btn-primary btn-sm rounded-pill">Ver Listado</a></div></div></div></div>`).join('')}</div></section></div>`;
}

function renderFactsPageLayout(pageState) {
    const allCategories = [...new Set(facts.filter(f => f.collection_id === pageState.collectionId).map(f => f.category))];
    const allSources = [...new Set(facts.filter(f => f.collection_id === pageState.collectionId).flatMap(f => f.sources))];
    mainContent.innerHTML = `<div class="p-4"><a href="index.html" class="btn btn-outline-secondary mb-4 rounded-pill px-4 py-2 fw-bold animate__animated animate__fadeInLeft">← Volver a Colecciones</a><div class="alert alert-info p-4 rounded-3 mb-4 border-0 shadow-sm animate__animated animate__fadeIn"><h3 class="alert-heading fw-bold text-info-emphasis">${pageState.collection.title}</h3><p class="mb-0 text-info-emphasis">${pageState.collection.description}</p></div><div class="card mb-4 shadow-sm border-0 rounded-3 p-4 animate__animated animate__fadeInUp"><h5 class="card-title text-primary fw-bold mb-3">Filtros</h5><div class="row g-3 align-items-end"><div class="col-md-3"><label class="form-label small text-muted">Fecha</label><input type="date" id="filter-date" class="form-control" value="${pageState.filters.date}"></div><div class="col-md-3"><label class="form-label small text-muted">Categoría</label><select id="filter-category" class="form-select"><option value="">Todas</option>${allCategories.map(c => `<option value="${c}" ${pageState.filters.category === c ? 'selected' : ''}>${c}</option>`).join('')}</select></div><div class="col-md-3"><label class="form-label small text-muted">Fuente</label><select id="filter-source" class="form-select"><option value="">Todas</option>${allSources.map(s => `<option value="${s}" ${pageState.filters.source === s ? 'selected' : ''}>${s}</option>`).join('')}</select></div><div class="col-md-3"><div class="d-flex align-items-center gap-3"><span class="fw-semibold text-secondary">Modo:</span><div class="btn-group" role="group"><input type="radio" class="btn-check" name="viewMode" id="irrestricto" ${pageState.viewMode==='irrestricto'?'checked':''}><label class="btn btn-outline-primary rounded-start-pill btn-sm" for="irrestricto">Irrestricto</label><input type="radio" class="btn-check" name="viewMode" id="curado" ${pageState.viewMode==='curado'?'checked':''}><label class="btn btn-outline-primary rounded-end-pill btn-sm" for="curado">Curado</label></div></div></div></div></div><div class="d-flex justify-content-between align-items-center mb-4"><div class="btn-group"><button id="list-view-btn" class="btn ${!pageState.mapView ? 'btn-primary' : 'btn-outline-primary'}">Lista</button><button id="map-view-btn" class="btn ${pageState.mapView ? 'btn-primary' : 'btn-outline-primary'}">Mapa</button></div><div class="input-group" style="max-width: 300px;"><input type="text" id="search-input" class="form-control" placeholder="Buscar por título..." value="${pageState.searchTerm}"><button class="btn btn-outline-secondary" type="button"><i class="bi bi-search"></i></button></div></div><div id="facts-content-wrapper"></div></div>`;
}
function renderFactsResults(pageState) {
    const getFilteredFacts = () => {
        let filtered = facts.filter(f => f.collection_id === pageState.collectionId);
        if (pageState.viewMode === 'curado') filtered = filtered.filter(f => getConsensusStatus(f, pageState.collection.consensus_algorithm).met);
        if (pageState.filters.date) filtered = filtered.filter(f => f.date === pageState.filters.date);
        if (pageState.filters.category) filtered = filtered.filter(f => f.category === pageState.filters.category);
        if (pageState.filters.source) filtered = filtered.filter(f => f.sources.includes(pageState.filters.source));
        if (pageState.searchTerm) filtered = filtered.filter(f => f.title.toLowerCase().includes(pageState.searchTerm.toLowerCase()));
        return filtered;
    };
    const getConsensusStatus = (fact, algorithm) => {
        const totalSources = 4; const sourceCount = fact.sources.length;
        switch (algorithm) {
            case 'absolute': return { met: sourceCount === totalSources, text: 'Absoluta' };
            case 'majority': return { met: sourceCount >= totalSources / 2, text: 'Mayoría Simple' };
            case 'multiple': return { met: sourceCount >= 2, text: 'Múltiples Menciones' };
            default: return { met: true, text: 'No Especificado' };
        }
    };
    const initializeMap = (factsToDisplay) => {
        const mapElement = document.getElementById('map');
        if (!mapElement || mapElement._leaflet_id) return;
        const mapCenter = factsToDisplay.length > 0 ? [factsToDisplay[0].latitude, factsToDisplay[0].longitude] : [-34.6037, -58.3816];
        const map = L.map('map').setView(mapCenter, 6);
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', { attribution: '&copy; OpenStreetMap' }).addTo(map);
        factsToDisplay.forEach(fact => { L.marker([fact.latitude, fact.longitude]).addTo(map).bindPopup(`<h6>${fact.title}</h6><p>${fact.description}</p>`); });
    };

    const resultsContainer = document.getElementById('facts-content-wrapper');
    const filteredFacts = getFilteredFacts();
    const factsHTML = filteredFacts.length > 0 ? filteredFacts.map(fact => `<div class="col-md-6 col-lg-4"><div class="card h-100 shadow-sm border-0 rounded-3 custom-card-hover"><div class="card-body d-flex flex-column"><div class="d-flex justify-content-between align-items-start mb-2"><h6 class="card-title text-primary fw-bold mb-0" style="cursor: pointer;">${fact.title}</h6>${pageState.viewMode === 'curado' ? `<span class="badge ${getConsensusStatus(fact, pageState.collection.consensus_algorithm).met ? 'bg-success' : 'bg-danger'} rounded-pill ms-2">${consensusLabels[pageState.collection.consensus_algorithm]}</span>` : ''}</div><span class="badge bg-light text-dark-emphasis rounded-pill mb-2 align-self-start">${fact.category}</span><p class="card-text text-muted small flex-grow-1 mb-3">${fact.description}</p><div class="d-flex justify-content-between align-items-center mt-auto pt-3 border-top"><small class="text-muted">Fecha: ${fact.date}</small><button class="btn btn-outline-danger btn-sm rounded-pill" data-delete-fact-id="${fact.id}">Solicitar Eliminación</button></div></div></div></div>`).join('') : `<div class="col-12"><div class="card shadow-sm border-0 rounded-3 text-center p-5"><p class="text-muted lead">No hay hechos que coincidan con los filtros aplicados.</p></div></div>`;
    resultsContainer.innerHTML = pageState.mapView ? `<div id="map" style="height: 60vh; border-radius: 8px;"></div>` : `<div class="row g-4">${factsHTML}</div>`;
    if (pageState.mapView) initializeMap(filteredFacts);
}

function renderLoginRegister() {
    mainContent.innerHTML = `<div class="d-flex justify-content-center align-items-center" style="min-height: 80vh;"><div class="card shadow-sm border-0 rounded-3 p-4" style="max-width: 450px; width: 100%;"><div id="login-register-container"></div></div></div>`;
}
function renderLoginForm() {
    document.getElementById('login-register-container').innerHTML = `<div class="card-body"><h5 class="card-title text-primary fw-bold text-center mb-4">Iniciar Sesión</h5><div class="d-grid gap-2 mt-4"><button id="login-contrib-btn" class="btn btn-primary fw-bold">Entrar como Contribuyente</button><button id="login-admin-btn" class="btn btn-secondary fw-bold">Entrar como Administrador</button></div><hr><div class="text-center mt-3"><small class="text-muted">¿No tienes cuenta? <span id="show-register" class="text-primary fw-bold" style="cursor:pointer;">Regístrate aquí</span></small></div></div>`;
}
function renderRegisterForm() {
    document.getElementById('login-register-container').innerHTML = `<div class="card-body"><h5 class="card-title text-primary fw-bold text-center mb-2">Registro</h5><form id="register-form"><div class="mb-2"><label class="form-label text-muted small">Nombre</label><input type="text" class="form-control" required></div><div class="mb-2"><label class="form-label text-muted small">Apellido</label><input type="text" class="form-control" required></div><div class="mb-2"><label class="form-label text-muted small">Fecha de Nacimiento</label><input type="date" class="form-control" required max="${new Date().toISOString().split("T")[0]}"></div><div class="mb-2"><label class="form-label text-muted small">Correo Electrónico</label><input type="email" class="form-control" required></div><div class="mb-2"><label class="form-label text-muted small">Contraseña</label><input type="password" class="form-control" required></div><div class="mb-2"><label class="form-label text-muted small">Confirmar Contraseña</label><input type="password" class="form-control" required></div><div class="form-check mb-3"><input type="checkbox" class="form-check-input" id="termsCheck" required><label class="form-check-label small" for="termsCheck">Acepto los <a href="#" data-show-terms class="link-primary">términos y condiciones</a>.</label></div><div class="d-grid"><button type="submit" class="btn btn-primary fw-bold">Registrarme</button></div></form><div class="text-center mt-3"><small class="text-muted">¿Ya tienes cuenta? <span id="show-login" class="text-primary fw-bold" style="cursor:pointer;">Inicia sesión</span></small></div></div>`;
}

function renderAdminPanelLayout(pageState) {
    mainContent.innerHTML = `<div class="p-4 animate__animated animate__fadeIn"><div class="card shadow-lg border-0 rounded-3"><div class="card-header bg-primary text-white border-bottom-0 p-4 rounded-top-3"><h3 class="fw-bold mb-0">Panel de Administración</h3></div><div class="card-body p-4"><ul class="nav nav-pills nav-justified mb-4"><li class="nav-item"><button id="admin-tab-collections" class="nav-link ${pageState.activeTab === 'collections' ? 'active' : ''}">Gestionar Colecciones</button></li><li class="nav-item"><button id="admin-tab-requests" class="nav-link ${pageState.activeTab === 'requests' ? 'active' : ''}">Revisar Solicitudes</button></li></ul><div id="admin-tab-content-wrapper"></div></div></div></div>`;
}
function renderAdminPanelContent(pageState) {
    let filteredRequests = [...requests];
    if (pageState.filters.type) filteredRequests = filteredRequests.filter(r => r.type === pageState.filters.type);
    if (pageState.filters.status) filteredRequests = filteredRequests.filter(r => r.status === pageState.filters.status);
    const collectionsRows = collections.map(c => `<tr><td>${c.title}</td><td>${c.source}</td><td><span class="badge bg-primary rounded-pill">${consensusLabels[c.consensus_algorithm]}</span></td><td class="text-end"><button class="btn btn-primary btn-sm rounded-pill" data-edit-id="${c.handle_id}">Editar</button></td></tr>`).join('');
    const requestsRows = filteredRequests.map(r => `<tr><td>${r.title}</td><td><span class="badge bg-secondary">${r.type}</span></td><td><span class="badge bg-${r.status === 'pendiente' ? 'warning' : (r.status === 'aprobada' ? 'success' : 'danger')}">${r.status}</span></td><td class="text-end">${r.status === 'pendiente' ? `<button class="btn btn-primary btn-sm rounded-pill" data-review-req-id="${r.id}">Evaluar</button>` : ''}</td></tr>`).join('');
    const requestFiltersHTML = `<div class="card card-body mb-3 bg-light border-0"><div class="row g-3 align-items-center"><div class="col-md-4"><label class="form-label small">Filtrar por tipo</label><select class="form-select form-select-sm" id="admin-filter-type"><option value="">Todos</option><option value="Nuevo Hecho" ${pageState.filters.type === 'Nuevo Hecho' ? 'selected' : ''}>Nuevo Hecho</option><option value="Eliminación" ${pageState.filters.type === 'Eliminación' ? 'selected' : ''}>Eliminación</option></select></div><div class="col-md-4"><label class="form-label small">Filtrar por estado</label><select class="form-select form-select-sm" id="admin-filter-status"><option value="">Todos</option><option value="pendiente" ${pageState.filters.status === 'pendiente' ? 'selected' : ''}>Pendiente</option><option value="aprobada" ${pageState.filters.status === 'aprobada' ? 'selected' : ''}>Aprobada</option><option value="rechazada" ${pageState.filters.status === 'rechazada' ? 'selected' : ''}>Rechazada</option></select></div></div></div>`;
    const contentContainer = document.getElementById('admin-tab-content-wrapper');
    if (pageState.activeTab === 'collections') {
        contentContainer.innerHTML = `<div class="d-flex gap-2 mb-4"><button class="btn btn-success rounded-pill px-4 fw-bold" data-create-collection>+ Nueva Colección</button><button class="btn btn-info rounded-pill px-4 fw-bold text-white" data-import-csv><i class="bi bi-upload me-2"></i>Importar CSV</button></div><div class="table-responsive rounded-3 overflow-hidden shadow-sm"><table class="table table-striped table-hover mb-0"><thead class="bg-light"><tr><th class="text-secondary fw-bold">Título</th><th class="text-secondary fw-bold">Fuente</th><th class="text-secondary fw-bold">Algoritmo</th><th class="text-end text-secondary fw-bold">Acciones</th></tr></thead><tbody>${collectionsRows}</tbody></table></div>`;
    } else {
        contentContainer.innerHTML = `${requestFiltersHTML}<div class="table-responsive rounded-3 overflow-hidden shadow-sm"><table class="table table-striped table-hover mb-0"><thead class="bg-light"><tr><th class="text-secondary fw-bold">Título</th><th class="text-secondary fw-bold">Tipo</th><th class="text-secondary fw-bold">Estado</th><th class="text-end text-secondary fw-bold">Acciones</th></tr></thead><tbody>${requestsRows}</tbody></table></div>`;
    }
}

function renderContributorPanelLayout(pageState) {
    mainContent.innerHTML = `<div class="p-4 animate__animated animate__fadeIn"><div class="card shadow-lg border-0 rounded-3"><div class="card-header bg-primary text-white border-bottom-0 p-4 rounded-top-3"><h3 class="fw-bold mb-0">Mi Panel de Contribuyente</h3></div><div class="card-body p-4"><ul class="nav nav-pills nav-justified mb-4"><li class="nav-item"><button id="contrib-tab-my-facts" class="nav-link ${pageState.activeTab === 'my-facts' ? 'active' : ''}">Mis Hechos Publicados</button></li><li class="nav-item"><button id="contrib-tab-my-requests" class="nav-link ${pageState.activeTab === 'my-requests' ? 'active' : ''}">Estado de Solicitudes</button></li></ul><div id="contrib-tab-content-wrapper"></div></div></div></div>`;
}
function renderContributorPanelContent(pageState) {
    const myFacts = facts.filter(f => f.userId === AppState.currentUser.id);
    const factsRows = myFacts.map(f => `<tr><td>${f.title}</td><td>${f.date}</td><td><span class="badge bg-success">Publicado</span></td><td class="text-end"><button class="btn btn-primary btn-sm rounded-pill" data-edit-fact-id="${f.id}">Editar</button></td></tr>`).join('');
    const myFactsContent = `<div class="table-responsive rounded-3 overflow-hidden shadow-sm">${myFacts.length > 0 ? `<table class="table table-striped table-hover mb-0"><thead class="bg-light"><tr><th class="text-secondary fw-bold">Título</th><th class="text-secondary fw-bold">Fecha</th><th class="text-secondary fw-bold">Estado</th><th class="text-end text-secondary fw-bold">Acciones</th></tr></thead><tbody>${factsRows}</tbody></table>` : `<div class="text-center text-muted p-5">Aún no tienes hechos publicados.</div>`}</div>`;
    let myRequests = requests.filter(r => r.userId === AppState.currentUser.id);
    if (pageState.filters.type) myRequests = myRequests.filter(r => r.type === pageState.filters.type);
    if (pageState.filters.status) myRequests = myRequests.filter(r => r.status === pageState.filters.status);
    const statusBadges = { pendiente: 'warning', aprobada: 'success', rechazada: 'danger' };
    const requestsRows = myRequests.map(r => `<tr><td>${r.title}</td><td><span class="badge bg-secondary">${r.type}</span></td><td><span class="badge bg-${statusBadges[r.status]}">${r.status}</span></td></tr>`).join('');
    const requestFiltersHTML = `<div class="card card-body mb-3 bg-light border-0"><div class="row g-3 align-items-center"><div class="col-md-4"><label class="form-label small">Filtrar por tipo</label><select class="form-select form-select-sm" id="contrib-filter-type"><option value="">Todos</option><option value="Nuevo Hecho" ${pageState.filters.type === 'Nuevo Hecho' ? 'selected' : ''}>Nuevo Hecho</option><option value="Eliminación" ${pageState.filters.type === 'Eliminación' ? 'selected' : ''}>Eliminación</option></select></div><div class="col-md-4"><label class="form-label small">Filtrar por estado</label><select class="form-select form-select-sm" id="contrib-filter-status"><option value="">Todos</option><option value="pendiente" ${pageState.filters.status === 'pendiente' ? 'selected' : ''}>Pendiente</option><option value="aprobada" ${pageState.filters.status === 'aprobada' ? 'selected' : ''}>Aprobada</option><option value="rechazada" ${pageState.filters.status === 'rechazada' ? 'selected' : ''}>Rechazada</option></select></div></div></div>`;
    const myRequestsContent = `${requestFiltersHTML}<div class="table-responsive rounded-3 overflow-hidden shadow-sm">${myRequests.length > 0 ? `<table class="table table-striped table-hover mb-0"><thead class="bg-light"><tr><th class="text-secondary fw-bold">Título</th><th class="text-secondary fw-bold">Tipo</th><th class="text-secondary fw-bold">Estado</th></tr></thead><tbody>${requestsRows}</tbody></table>` : `<div class="text-center text-muted p-5">No has realizado ninguna solicitud todavía.</div>`}</div>`;
    const contentContainer = document.getElementById('contrib-tab-content-wrapper');
    contentContainer.innerHTML = pageState.activeTab === 'my-facts' ? myFactsContent : myRequestsContent;
}

function renderProfilePage() {
    const user = AppState.currentUser;
    mainContent.innerHTML = `<div class="p-4 animate__animated animate__fadeIn"><div class="card shadow-lg border-0 rounded-3"><div class="card-header bg-primary text-white border-bottom-0 p-4 rounded-top-3"><h3 class="fw-bold mb-0">Mi Perfil</h3></div><div class="card-body p-4"><div id="alert-placeholder"></div><form id="profile-form"><div class="row g-3"><div class="col-md-6"><label class="form-label">Nombre</label><input type="text" name="name" class="form-control" value="${user.name}" required></div><div class="col-md-6"><label class="form-label">Apellido</label><input type="text" name="lastName" class="form-control" value="${user.lastName || ''}" required></div><div class="col-12"><label class="form-label">Correo Electrónico</label><input type="email" class="form-control" value="${user.email || ''}" disabled></div><div class="col-12"><label class="form-label">Fecha de Nacimiento</label><input type="date" name="birthDate" class="form-control" value="${user.birthDate || ''}" required></div><div class="col-12 mt-4"><button type="submit" class="btn btn-primary rounded-pill px-4">Guardar Cambios</button></div></div></form></div></div></div>`;
}

function renderCollectionModal(collection, reRenderCallback) {
    const consensusOptions = Object.entries(consensusLabels).map(([value, label]) => `<option value="${value}" ${collection?.consensus_algorithm === value ? 'selected' : ''}>${label}</option>`).join('');
    const sourceOptions = availableSources.map(source => `<option value="${source}" ${collection?.source === source ? 'selected' : ''}>${source}</option>`).join('');
    modalContainer.innerHTML = `<div class="modal fade" id="collection-modal" tabindex="-1"><div class="modal-dialog modal-lg modal-dialog-centered"><div class="modal-content rounded-4 shadow-lg border-0"><form id="collection-form"><div class="modal-header border-bottom-0 pb-0"><h5 class="modal-title text-primary fw-bold">${collection ? 'Editar' : 'Crear'} Colección</h5><button type="button" class="btn-close" data-bs-dismiss="modal"></button></div><div class="modal-body p-4 pt-0"><div class="mb-3"><label class="form-label fw-semibold">Título</label><input type="text" name="title" class="form-control" value="${collection?.title || ''}" required></div><div class="mb-3"><label class="form-label fw-semibold">Descripción</label><textarea name="description" class="form-control" rows="3" required>${collection?.description || ''}</textarea></div><div class="mb-3"><label class="form-label fw-semibold">Fuente</label><select name="source" class="form-select">${sourceOptions}</select></div><div class="mb-4"><label class="form-label fw-semibold">Algoritmo</label><select name="consensus_algorithm" class="form-select">${consensusOptions}</select></div></div><div class="modal-footer pt-3 border-top"><button type="button" class="btn btn-secondary rounded-pill px-4" data-bs-dismiss="modal">Cancelar</button><button type="submit" class="btn btn-primary rounded-pill px-4">Guardar</button></div></form></div></div></div>`;
    modalContainer.querySelector('#collection-form').addEventListener('submit', (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);
        const title = formData.get('title').trim(), description = formData.get('description').trim(), source = formData.get('source').trim(), consensus_algorithm = formData.get('consensus_algorithm');
        if (!title || !description || !source) return;
        if (collection) {
            const index = collections.findIndex(c => c.handle_id === collection.handle_id);
            if (index > -1) collections[index] = { ...collections[index], title, description, source, consensus_algorithm };
        } else {
            const newId = collections.length > 0 ? Math.max(...collections.map(c => c.handle_id)) + 1 : 1;
            collections.push({ handle_id: newId, title, description, source, consensus_algorithm });
        }
        saveData();
        bootstrap.Modal.getInstance(modalContainer.querySelector('#collection-modal'))?.hide();
        reRenderCallback();
    });
}
function renderFactModal(fact = null) {
    const isEditing = fact !== null;
    const categoryOptions = availableCategories.map(cat => `<option value="${cat}" ${fact?.category === cat ? 'selected' : ''}>${cat}</option>`).join('');
    modalContainer.innerHTML = `<div class="modal fade" id="fact-modal" tabindex="-1"><div class="modal-dialog modal-lg modal-dialog-centered"><div class="modal-content rounded-4 shadow-lg border-0"><form><div class="modal-header"><h5 class="modal-title text-primary fw-bold">${isEditing ? 'Editar' : 'Subir'} Hecho</h5><button type="button" class="btn-close" data-bs-dismiss="modal"></button></div><div class="modal-body"><p class="text-muted small">${isEditing ? 'Modifica los datos de tu hecho.' : 'Tu sugerencia será revisada por un administrador.'}</p><div class="mb-3"><label class="form-label">Título</label><input type="text" class="form-control" value="${fact?.title || ''}" required></div><div class="mb-3"><label class="form-label">Descripción</label><textarea class="form-control" rows="3" required>${fact?.description || ''}</textarea></div><div class="row"><div class="col-md-6 mb-3"><label class="form-label">Categoría</label><select class="form-select">${categoryOptions}</select></div><div class="col-md-6 mb-3"><label class="form-label">Fecha</label><input type="date" value="${fact?.date || ''}" class="form-control"></div></div><div class="mb-3"><label class="form-label">Archivo Multimedia</label><input class="form-control" type="file" accept="image/*,video/*">${fact?.multimediaFile ? `<small class="d-block mt-1 text-muted">Archivo actual: ${fact.multimediaFile}</small>`: ''}</div></div><div class="modal-footer"><button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button><button type="submit" class="btn btn-primary">${isEditing ? 'Guardar Cambios' : 'Enviar Sugerencia'}</button></div></form></div></div></div>`;
}
function renderFactDeletionModal(factId) {
    const fact = facts.find(f => f.id === factId);
    modalContainer.innerHTML = `<div class="modal fade" id="fact-deletion-modal" tabindex="-1"><div class="modal-dialog modal-dialog-centered"><div class="modal-content"><form><div class="modal-header"><h5 class="modal-title text-danger fw-bold">Solicitar Eliminación</h5><button type="button" class="btn-close" data-bs-dismiss="modal"></button></div><div class="modal-body"><p>Estás solicitando eliminar: <strong>${fact.title}</strong></p><div class="mb-3"><label for="justification" class="form-label">Justificación (mín. 500 caracteres)</label><textarea class="form-control" id="justification" rows="5" required minlength="500"></textarea><small class="form-text text-muted"><span id="char-count">0</span> / 500</small></div></div><div class="modal-footer"><button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button><button type="submit" class="btn btn-danger" disabled>Enviar</button></div></form></div></div></div>`;
    const textarea = modalContainer.querySelector('#justification'), charCount = modalContainer.querySelector('#char-count'), submitBtn = modalContainer.querySelector('button[type="submit"]');
    textarea.addEventListener('input', () => { const count = textarea.value.length; charCount.textContent = count; submitBtn.disabled = count < 500; });
}
function renderRequestReviewModal(reqId) {
    const request = requests.find(r => r.id === reqId);
    modalContainer.innerHTML = `<div class="modal fade" id="req-review-modal" tabindex="-1"><div class="modal-dialog modal-dialog-centered"><div class="modal-content"><div class="modal-header"><h5 class="modal-title text-primary fw-bold">Evaluar Solicitud</h5><button type="button" class="btn-close" data-bs-dismiss="modal"></button></div><div class="modal-body"><h6>${request.type}: "${request.title}"</h6><p><strong>Detalles:</strong><br>Aquí se mostrarían los detalles completos.</p></div><div class="modal-footer"><button type="button" class="btn btn-danger">Rechazar</button><button type="button" class="btn btn-success">Aprobar</button></div></div></div></div>`;
}
function renderCsvImportModal() {
    modalContainer.innerHTML = `<div class="modal fade" id="csv-import-modal" tabindex="-1"><div class="modal-dialog modal-dialog-centered"><div class="modal-content"><form><div class="modal-header"><h5 class="modal-title text-primary fw-bold">Importar desde CSV</h5><button type="button" class="btn-close" data-bs-dismiss="modal"></button></div><div class="modal-body"><p class="text-muted small">Selecciona un archivo .csv para la carga masiva de hechos.</p><div class="mb-3"><label for="csvFile" class="form-label">Archivo CSV</label><input class="form-control" type="file" id="csvFile" accept=".csv" required></div></div><div class="modal-footer"><button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button><button type="submit" class="btn btn-primary">Importar</button></div></form></div></div></div>`;
}
function renderTermsModal() {
    modalContainer.innerHTML = `<div class="modal fade" id="terms-modal" tabindex="-1"><div class="modal-dialog modal-lg modal-dialog-centered modal-dialog-scrollable"><div class="modal-content"><div class="modal-header"><h5 class="modal-title text-primary fw-bold">Términos y Condiciones</h5><button type="button" class="btn-close" data-bs-dismiss="modal"></button></div><div class="modal-body"><h6>1. Aceptación de los Términos</h6><p>Bienvenido a MetaMapa. Al acceder y utilizar nuestra plataforma, usted acepta estar sujeto a los siguientes términos y condiciones de servicio. Si no está de acuerdo con alguna parte de los términos, no podrá utilizar el servicio.</p><h6>2. Uso de la Plataforma</h6><p>Usted se compromete a utilizar la plataforma de manera legal, responsable y ética. Queda prohibido subir contenido que sea falso, engañoso, difamatorio, que infrinja derechos de autor, o que sea de naturaleza maliciosa.</p><h6>3. Contenido del Usuario</h6><p>Al enviar contenido a MetaMapa (textos, imágenes, etc.), usted otorga a MetaMapa una licencia mundial, no exclusiva, perpetua, irrevocable y libre de regalías para usar, reproducir, modificar, adaptar, publicar, traducir y mostrar dicho Contenido en conexión con el servicio. Usted declara y garantiza que posee todos los derechos necesarios para otorgar esta licencia.</p><h6>4. Política de Privacidad y Datos</h6><p>Al registrarse, usted acepta que podemos recopilar y almacenar la información proporcionada (nombre, correo, etc.) para gestionar su cuenta y contribuciones. Nos comprometemos a proteger su información y a no compartirla con terceros para fines comerciales sin su consentimiento explícito.</p><h6>5. Moderación de Contenido</h6><p>MetaMapa se reserva el derecho de revisar, filtrar, modificar o eliminar cualquier contenido enviado por los usuarios a nuestra entera discreción y sin previo aviso, si viola estos términos o no cumple con nuestros estándares de calidad y veracidad.</p></div><div class="modal-footer"><button type="button" class="btn btn-primary" data-bs-dismiss="modal">Entendido</button></div></div></div></div>`;
}

document.addEventListener('DOMContentLoaded', () => {
    const toggler = document.getElementById('sidebar-toggler-btn');
    if (toggler) toggler.addEventListener('click', () => sidebar.classList.toggle('show'));
});