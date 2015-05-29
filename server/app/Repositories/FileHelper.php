<?php

namespace App\Repositories;

use App\Interfaces\FileHelper as FileHelperInterface;

class FileHelper implements FileHelperInterface
{
    public function deleteStoryMediaFile($id, $filename)
    {
        if (\Storage::disk('local')->exists($id . '/' . $filename)) {
            \Storage::delete($id . '/' . $filename);
        }
    }


    public function getStoryMediaFiles($id)
    {
        $retVal = null;

        if (\Storage::allFiles($id)) {
            $allFiles = array();

            foreach (\Storage::allFiles($id) as $fileData) {
                $fileInfo = pathinfo($fileData);

                $file = array(
                    'file'       => $fileInfo['basename'],
                    'folder'     => $fileInfo['dirname'],
                    'extension'  => $fileInfo['extension'],
                    'size'       => \Storage::size($fileData),
                    'created_at' => date("Y-m-d H:i:s", filectime(public_path() . '/media/' . $fileData)),
                );

                $allFiles[] = $file;
            }

            $headers['Content-Type'] = 'application/json; charset=utf-8';
            $retVal = response()->json($allFiles, 200, $headers, JSON_UNESCAPED_UNICODE);
        } else {
            $errorMessage = [
                'message' => 'No files found'
            ];
            $retVal = response()->view('errors.error', $errorMessage, 404, []);
        }

        return $retVal;
    }


    public function storeStoryMediaFile($id, $file)
    {
        if ($this->validateFile($file)) {
            $mediaPath = 'media/' . $id;
            $filename = $file->getClientOriginalName();
            $upload = $file->move($mediaPath, $filename);
        }
    }


    public function validateFile($file)
    {
        $retVal = null;

        $filesize = $file->getClientSize();
        $mimeType = $file->getClientMimeType();

        $fileextension = $file->guessClientExtension();
        if ((($mimeType == 'image/jpeg' || $mimeType == 'image/png' || $mimeType == 'video/mp4') && $filesize <= 5000000) &&
            ($fileextension == 'jpg' || $fileextension == 'jpeg' || $fileextension == 'png' || $fileextension == 'mp4')
        ) {
            $retVal = true;
        } else {
            $retVal = false;
        }

        return $retVal;
    }


}