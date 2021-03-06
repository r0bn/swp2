<?php

use Illuminate\Http\Request;

/*
|--------------------------------------------------------------------------
| Application Routes
|--------------------------------------------------------------------------
|
| Here is where you can register all of the routes for an application.
| It's a breeze. Simply tell Laravel the URIs it should respond to
| and give it the controller to call when that URI is requested.
|
*/


/**
 * Routes for stories (authorentool)
 *
 * Determines the routes for every stroy handling interaction.
 */

Route::get('story', 'StorytellarController@getStories');

Route::get('story/open', 'StorytellarController@getOpenStories');

Route::get('story/{id}', 'StorytellarController@getStory');

Route::get('story/{id}/media', 'StorytellarController@getStoryMediaFiles');


Route::post('story/{id}', 'StorytellarController@updateStory');

Route::post('story', 'StorytellarController@createStory');

Route::delete('story/{id}/media/{filename}', 'StorytellarController@deleteFile');

Route::delete('story/{id}', 'StorytellarController@deleteStory');

Route::put('story/{id}/media', 'StorytellarController@addFile');


/**
 * Temporary routes for stories (player)
 *
 * This determines only the routes for downloading.
 */

Route::get('temp', 'StorytellarController@getTempStories');

Route::get('temp/{id}', 'StorytellarController@getTempStory');


/**
 * Routes for xml schema validation
 */

Route::get('docs/tests/xmlschemavalidation', 'StorytellarController@getXmlSchemaValidation');

Route::post('docs/tests/xmlschemavalidation', 'StorytellarController@postXmlSchemaValidation');


/**
 * Routes for authentication and user management
 */

//Route::post('register', 'RegisterController@register');

Route::post('authenticate', 'AuthenticateController@authenticate');

Route::get('reauthenticate', ['middleware' => ['jwt.refresh'], 'uses' => 'AuthenticateController@reauthenticate']);

Route::get('user', ['middleware' => ['jwt.auth'], 'uses' => 'StorytellarController@getUserStories']);

Route::controllers([
    'auth' => 'Auth\AuthController',
    'password' => 'Auth\PasswordController',
]);

Route::get('account/password', function () {
    return view('auth.password');
});

Route::get('account', function () {
    return view('auth.register');
});

Route::get('account/register', function () {
    return view('auth.register');
});

Route::get('register/verify/{token}', 'RegisterController@verifyUser');
