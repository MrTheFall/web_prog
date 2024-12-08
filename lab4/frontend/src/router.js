import { createRouter, createWebHistory } from 'vue-router'; // Import createRouter and createWebHistory
import Registration from "@/views/Registration";
import Main from "@/views/Main";
import Logout from "@/views/Logout";
import NotFoundError from "@/views/Error";

// Create the router instance
const router = createRouter({
    history: createWebHistory(), // Use createWebHistory for history mode
    routes: [
        {
            path: '/',
            name: 'default-page',
            component: Registration,
            beforeEnter: (to, from, next) => {
                const getCookie = (name) => {
                    const value = `; ${document.cookie}`;
                    const parts = value.split(`; ${name}=`);
                    if (parts.length === 2) return parts.pop().split(';').shift();
                };
            
                // Check if the "jwt" cookie exists
                (getCookie("jwt") !== undefined) ? next({ name: 'app-page' }) : next({ name: 'auth-page' });
            }
        },
        {
            path: '/auth',
            name: 'auth-page',
            component: Registration,
        },
        {
            path: '/logout',
            name: 'logout',
            component: Logout,
        },
        {
            path: '/app',
            name: 'app-page',
            component: Main,
            beforeEnter: (to, from, next) => {
                // Function to get a cookie by name
                const getCookie = (name) => {
                    const value = `; ${document.cookie}`;
                    const parts = value.split(`; ${name}=`);
                    if (parts.length === 2) return parts.pop().split(';').shift();
                };
        
                // Check if the "jwt" cookie exists
                if (getCookie("jwt")) {
                    next();
                } else {
                    next({
                        name: 'error-page-app',
                    });
                }
            }        
        },
        {
            path: '/*',
            name: 'error-page',
            component: NotFoundError,
            props: {
                default: true,
                errorCode: "404",
                errorMessage: "Страница не найдена"
            }
        },
        {
            path: '/error',
            name: 'error-page-app',
            component: NotFoundError,
            props: {
                default: true,
                errorCode: "401",
                errorMessage: "Нет доступа!"
            }
        }
    ]
});

export default router; // Export the router instance
