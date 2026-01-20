<img width="1113" height="1207" alt="image" src="https://github.com/user-attachments/assets/cb4a357b-91c9-47c1-970b-3b871236318f" /><!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestor de Incidencias | Documentación</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        
        body {
            background-color: #f8f9fa;
            color: #333;
            line-height: 1.6;
            padding: 20px;
            max-width: 1200px;
            margin: 0 auto;
        }
        
        header {
            background: linear-gradient(135deg, #1e3c72, #2a5298);
            color: white;
            padding: 40px 30px;
            border-radius: 10px;
            margin-bottom: 30px;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
        }
        
        h1 {
            font-size: 2.5rem;
            margin-bottom: 10px;
            font-weight: 600;
        }
        
        .subtitle {
            font-size: 1.2rem;
            opacity: 0.9;
            font-weight: 300;
        }
        
        .content-container {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 30px;
            margin-bottom: 30px;
        }
        
        @media (max-width: 768px) {
            .content-container {
                grid-template-columns: 1fr;
            }
        }
        
        .card {
            background: white;
            border-radius: 10px;
            padding: 25px;
            box-shadow: 0 3px 10px rgba(0, 0, 0, 0.08);
            transition: transform 0.3s ease;
        }
        
        .card:hover {
            transform: translateY(-5px);
        }
        
        .card h2 {
            color: #2a5298;
            border-bottom: 2px solid #eaeaea;
            padding-bottom: 10px;
            margin-bottom: 20px;
            font-size: 1.5rem;
        }
        
        .highlight {
            background-color: #eef4ff;
            padding: 25px;
            border-radius: 10px;
            border-left: 5px solid #2a5298;
            margin: 25px 0;
        }
        
        .highlight h3 {
            color: #2a5298;
            margin-bottom: 15px;
        }
        
        .feature-list {
            list-style-type: none;
        }
        
        .feature-list li {
            padding: 8px 0;
            padding-left: 25px;
            position: relative;
        }
        
        .feature-list li:before {
            content: "✓";
            color: #2a5298;
            position: absolute;
            left: 0;
            font-weight: bold;
        }
        
        .credentials {
            background-color: #f8f9fa;
            border-radius: 8px;
            padding: 20px;
            margin: 25px 0;
            border: 1px solid #eaeaea;
        }
        
        .credentials h3 {
            color: #2a5298;
            margin-bottom: 15px;
        }
        
        .credentials-table {
            width: 100%;
            border-collapse: collapse;
        }
        
        .credentials-table th {
            background-color: #eef4ff;
            padding: 12px 15px;
            text-align: left;
            font-weight: 600;
            border-bottom: 2px solid #ddd;
        }
        
        .credentials-table td {
            padding: 12px 15px;
            border-bottom: 1px solid #eee;
        }
        
        .credentials-table tr:hover {
            background-color: #f5f7ff;
        }
        
        .screenshots {
            display: flex;
            flex-direction: column;
            gap: 25px;
            margin-top: 30px;
        }
        
        .screenshot-container {
            background: white;
            border-radius: 10px;
            padding: 20px;
            box-shadow: 0 3px 10px rgba(0, 0, 0, 0.08);
        }
        
        .screenshot-container h3 {
            color: #2a5298;
            margin-bottom: 15px;
            padding-bottom: 10px;
            border-bottom: 1px solid #eee;
        }
        
        .screenshot-container img {
            width: 100%;
            border-radius: 5px;
            border: 1px solid #eee;
            transition: transform 0.3s ease;
        }
        
        .screenshot-container img:hover {
            transform: scale(1.01);
        }
        
        .tech-badge {
            display: inline-block;
            background-color: #2a5298;
            color: white;
            padding: 5px 12px;
            border-radius: 20px;
            font-size: 0.85rem;
            margin: 5px 5px 5px 0;
        }
        
        .role-badge {
            display: inline-block;
            padding: 4px 10px;
            border-radius: 4px;
            font-size: 0.8rem;
            font-weight: 600;
            margin-right: 8px;
        }
        
        .user-badge {
            background-color: #e3f2fd;
            color: #1565c0;
        }
        
        .tech-badge-role {
            background-color: #e8f5e9;
            color: #2e7d32;
        }
        
        .admin-badge {
            background-color: #fce4ec;
            color: #ad1457;
        }
        
        footer {
            text-align: center;
            margin-top: 40px;
            padding: 20px;
            color: #666;
            border-top: 1px solid #eee;
            font-size: 0.9rem;
        }
    </style>
</head>
<body>
    <header>
        <h1>Gestor de Incidencias</h1>
        <p class="subtitle">Aplicación de gestión de incidencias empresarial desarrollada en JavaFX</p>
        <div style="margin-top: 20px;">
            <span class="tech-badge">Java</span>
            <span class="tech-badge">JavaFX</span>
            <span class="tech-badge">Swing</span>
            <span class="tech-badge">DAO Pattern</span>
            <span class="tech-badge">Javadoc</span>
        </div>
    </header>
    
    <div class="content-container">
        <div class="card">
            <h2>Descripción del Proyecto</h2>
            <p>Este proyecto consistió en el desarrollo de una aplicación de gestión de incidencias para una empresa real, implementando todos los objetivos obligatorios y opcionales.</p>
            
            <div class="highlight">
                <h3>Características Destacadas</h3>
                <ul class="feature-list">
                    <li>Desarrollado en <strong>JavaFX</strong> (con punto extra sobre Swing)</li>
                    <li>Javadoc completo y documentación exhaustiva</li>
                    <li>Opciones avanzadas de filtrado y ajustes</li>
                    <li>Código depurado y testeado para detección de errores</li>
                    <li>Arquitectura personalizada: modelo, DAO y gestor desarrollados manualmente</li>
                </ul>
            </div>
            
            <p>La aplicación implementa un sistema de roles completo con diferentes permisos y capacidades para cada tipo de usuario.</p>
        </div>
        
        <div class="card">
            <h2>Funcionalidades por Rol</h2>
            
            <h3><span class="role-badge user-badge">Usuario</span></h3>
            <ul class="feature-list">
                <li>Crear nuevas incidencias</li>
                <li>Editar incidencias propias</li>
                <li>Eliminar incidencias (baja lógica)</li>
                <li>Reabrir incidencias cerradas</li>
            </ul>
            
            <h3><span class="role-badge tech-badge-role">Técnico</span></h3>
            <ul class="feature-list">
                <li>Solucionar incidencias</li>
                <li>Poner incidencias en espera</li>
                <li>Cerrar y reabrir incidencias</li>
                <li>Editar prioridad de incidencias</li>
            </ul>
            
            <h3><span class="role-badge admin-badge">Administrador</span></h3>
            <ul class="feature-list">
                <li>Todas las funciones de usuarios y técnicos</li>
                <li>Asignar técnicos específicos a incidencias</li>
                <li>Borrado permanente de incidencias</li>
                <li>Acceso completo al sistema</li>
            </ul>
        </div>
    </div>
    
    <div class="card">
        <h2>Funcionalidades Adicionales</h2>
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(300px, 1fr)); gap: 20px;">
            <div>
                <h3>Sistema de Filtrado</h3>
                <ul class="feature-list">
                    <li>Filtrado por estado</li>
                    <li>Filtrado por prioridad</li>
                    <li>Mostrar/ocultar incidencias cerradas</li>
                    <li>Quitar etiquetas de botones</li>
                </ul>
            </div>
            
            <div>
                <h3>Visualización y Reportes</h3>
                <ul class="feature-list">
                    <li>Visualización completa de información de incidencias</li>
                    <li>Histórico de cambios por incidencia</li>
                    <li>Generación de informes en formato .txt</li>
                    <li>Reloj integrado en la interfaz</li>
                </ul>
            </div>
            
            <div>
                <h3>Características Técnicas</h3>
                <ul class="feature-list">
                    <li>Actualización automática cada 60 segundos</li>
                    <li>Logos de empresa y perfil de usuario (placeholders)</li>
                    <li>Interfaz intuitiva y profesional</li>
                    <li>Base de datos con persistencia de datos</li>
                </ul>
            </div>
        </div>
    </div>
    
    <div class="credentials">
        <h3>Credenciales de Acceso</h3>
        <p>La base de datos suministrada incluye las siguientes cuentas para pruebas:</p>
        
        <table class="credentials-table">
            <thead>
                <tr>
                    <th>Rol</th>
                    <th>Usuario</th>
                    <th>Contraseña</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td><span class="role-badge user-badge">Usuario</span></td>
                    <td>andoni@centro.edu<br>julen@centro.edu<br>victor@centro.edu<br>arkhyp@centro.edu<br>sergio@centro.edu</td>
                    <td>123</td>
                </tr>
                <tr>
                    <td><span class="role-badge tech-badge-role">Técnico</span></td>
                    <td>gorka@centro.edu<br>edrian@centro.edu</td>
                    <td>123</td>
                </tr>
                <tr>
                    <td><span class="role-badge admin-badge">Administrador</span></td>
                    <td>admin@centro.edu</td>
                    <td>123</td>
                </tr>
            </tbody>
        </table>
    </div>
    
    <h2 style="color: #2a5298; margin-top: 40px; margin-bottom: 20px;">Capturas de Pantalla</h2>
    
    <div class="screenshots">
        <div class="screenshot-container">
            <h3>Pantalla de Login</h3>
            <img src="resReadme/login.png" alt="Pantalla de inicio de sesión">
        </div>
        
        <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 25px;">
            <div class="screenshot-container">
                <h3>Panel de Usuario</h3>
                <img src="resReadme/user.png" alt="Panel de usuario">
            </div>
            
            <div class="screenshot-container">
                <h3>Nueva Incidencia</h3>
                <img src="resReadme/nueva.png" alt="Formulario de nueva incidencia">
            </div>
        </div>
        
        <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 25px;">
            <div class="screenshot-container">
                <h3>Panel de Técnico</h3>
                <img src="resReadme/tecnico.png" alt="Panel de técnico">
            </div>
            
            <div class="screenshot-container">
                <h3>Panel de Administrador</h3>
                <img src="resReadme/admin.png" alt="Panel de administrador">
            </div>
        </div>
    </div>
    
    <footer>
        <p>Proyecto desarrollado como parte de un encargo empresarial | Todos los objetivos implementados</p>
        <p style="margin-top: 10px;">© Gestor de Incidencias - Aplicación de gestión empresarial</p>
    </footer>
</body>
</html>
