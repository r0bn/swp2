<?php

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
 * Download Routes for stories (player)
 *
 * This determines the routes for downloading all story
 * metadata and a specific story by id.
 */
Route::get('stories', 'StorytellarController@getStories'); // deprecated

Route::get('tempstories', 'StorytellarController@getStoriesTemp'); // temporary

Route::get('story', 'StorytellarController@getStories');

Route::get('story/{id}', 'StorytellarController@getStory');


/**
 * Upload Routes for a story (authorentool)
 *
 * This determines the routes for uploading story files.
 */
Route::post('createstory', 'StorytellarController@createStory'); // deprecated

Route::get('testupload', function() { return View::make('test'); }); // temporary

Route::post('story', 'StorytellarController@createStory');