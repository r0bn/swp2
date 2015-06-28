<?php namespace App\Exceptions;

use Exception;
use Illuminate\Foundation\Exceptions\Handler as ExceptionHandler;
use Illuminate\Database\Eloquent\ModelNotFoundException;
use Tymon\JWTAuth\Exceptions\JWTException;
use Tymon\JWTAuth\JWTAuth;

class Handler extends ExceptionHandler
{

    /**
     * A list of the exception types that should not be reported.
     *
     * @var array
     */
    protected $dontReport = [
        'Symfony\Component\HttpKernel\Exception\HttpException'
    ];

    /**
     * Report or log an exception.
     *
     * This is a great spot to send exceptions to Sentry, Bugsnag, etc.
     *
     * @param  \Exception $e
     * @return void
     */
    public function report(Exception $e)
    {
        return parent::report($e);
    }

    /**
     * Render an exception into an HTTP response.
     *
     * @param  \Illuminate\Http\Request $request
     * @param  \Exception $e
     * @return \Illuminate\Http\Response
     */
    public function render($request, Exception $e)
    {
        $retVal = null;

        /*
        * If a Story Route with an non existing ID is called,
        * there will be a HTTP Response with 'Story not found' Error
        */
        if ($e instanceof ModelNotFoundException) {
            $errorMessage = [
                'message' => 'Story not found'
            ];
            $retVal = response()->view('errors.error', $errorMessage, 404, []);
        } else if ($e instanceof JWTAuth || $e instanceof JWTException) {
            $retVal = response()->json(['error' => 'authentication_error'], 400, [], JSON_UNESCAPED_UNICODE);
        } else {
            $retVal = parent::render($request, $e);
        }

        return $retVal;
    }

}
