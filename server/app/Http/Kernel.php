<?php namespace App\Http;

use Illuminate\Foundation\Http\Kernel as HttpKernel;

class Kernel extends HttpKernel
{

    /**
     * The application's global HTTP middleware stack.
     *
     * The following middlewares will be executed every time a Route is called
     *
     * @var array
     */
    protected $middleware = [
        'Illuminate\Foundation\Http\Middleware\CheckForMaintenanceMode',
//        'Illuminate\Cookie\Middleware\EncryptCookies',
//        'Illuminate\Cookie\Middleware\AddQueuedCookiesToResponse',
        'Illuminate\Session\Middleware\StartSession',
        'Illuminate\View\Middleware\ShareErrorsFromSession',
//		'App\Http\Middleware\VerifyCsrfToken',
    ];

    /**
     * The application's route middleware.
     *
     * The following middlewares will be executed only on specific Routes
     *
     * @var array
     */
    protected $routeMiddleware = [
        'auth'        => 'App\Http\Middleware\Authenticate',
        'auth.basic'  => 'Illuminate\Auth\Middleware\AuthenticateWithBasicAuth',
        'guest'       => 'App\Http\Middleware\RedirectIfAuthenticated',
        'jwt.auth'    => 'Tymon\JWTAuth\Middleware\GetUserFromToken',
        'jwt.refresh' => 'Tymon\JWTAuth\Middleware\RefreshToken',
    ];

}
