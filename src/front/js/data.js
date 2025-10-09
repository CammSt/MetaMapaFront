// js/data.js

const availableSources = ['Carga Manual', 'API Sudoeste', 'Datos Abiertos GCBA', 'API Externa B'];
const availableCategories = ['Crimen de Odio', 'Incendio Forestal', 'Accidente Vial', 'Contaminación Hídrica', 'Contaminación Aérea', 'Otro'];

// Intenta cargar los datos desde sessionStorage para simular persistencia en la sesión.
let collections = JSON.parse(sessionStorage.getItem('collections')) || [
    { handle_id: 1, title: "Desapariciones por crímenes de odio", description: "Recopilación de casos de desapariciones forzadas.", consensus_algorithm: 'majority', source: 'Carga Manual' },
    { handle_id: 2, title: "Incendios forestales en Argentina 2025", description: "Seguimiento de focos de incendio y áreas afectadas.", consensus_algorithm: 'multiple', source: 'API Sudoeste' },
];
let facts = JSON.parse(sessionStorage.getItem('facts')) || [
    { id: 101, userId: 1, collection_id: 1, title: "Caso J.P. (La Plata)", description: "Desaparición en contexto de manifestación.", category: "Crimen de Odio", date: "2024-05-10", sources: ['Fuente A', 'Fuente B', 'Fuente C'], latitude: -34.9205, longitude: -57.9536, multimediaFile: 'caso_jp.jpg' },
    { id: 102, userId: 2, collection_id: 1, title: "Incidente en Barrio Norte", description: "Agresión y posterior desaparición.", category: "Crimen de Odio", date: "2024-08-22", sources: ['Fuente A'], latitude: -34.5888, longitude: -58.4095, multimediaFile: null },
    { id: 201, userId: 1, collection_id: 2, title: "Incendio en Sierras de Córdoba", description: "Afectó 500 hectáreas, origen intencional.", category: "Incendio Forestal", date: "2025-01-15", sources: ['Fuente A', 'Fuente B'], latitude: -31.4201, longitude: -64.1888, multimediaFile: 'incendio_cordoba.mp4' },
];
let requests = JSON.parse(sessionStorage.getItem('requests')) || [
    { id: 1, userId: 1, type: 'Nuevo Hecho', title: "Emisiones no declaradas en Dock Sud", status: 'pendiente' },
    { id: 2, userId: 99, type: 'Eliminación', title: "Caso J.P. (La Plata)", status: 'pendiente' },
    { id: 3, userId: 1, type: 'Eliminación', title: "Incendio en Sierras de Córdoba", status: 'aprobada' },
    { id: 4, userId: 1, type: 'Nuevo Hecho', title: "Tala en Parque Pereyra", status: 'rechazada' },
];
const consensusLabels = { 'none': 'No Especificado', 'multiple': 'Múltiples Menciones', 'majority': 'Mayoría Simple', 'absolute': 'Absoluta' };

function saveData() {
    sessionStorage.setItem('collections', JSON.stringify(collections));
    sessionStorage.setItem('facts', JSON.stringify(facts));
    sessionStorage.setItem('requests', JSON.stringify(requests));
}
if (!sessionStorage.getItem('collections')) {
    saveData();
}