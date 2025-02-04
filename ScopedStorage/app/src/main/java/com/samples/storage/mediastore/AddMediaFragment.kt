/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.samples.storage.mediastore

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts.TakePicture
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import com.samples.storage.databinding.FragmentAddMediaBinding

// TODO(yrezgui): Add media from camera (video), from internet and generated one
class AddMediaFragment : Fragment() {
    private var _binding: FragmentAddMediaBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddMediaViewModel by viewModels()

    private val actionTakePicture = registerForActivityResult(TakePicture()) { success ->
        if (!success) {
            Log.d(tag, "Image taken FAIL: ${viewModel.temporaryMediaUri}")
            return@registerForActivityResult
        }

        Log.d(tag, "Image taken SUCCESS: ${viewModel.temporaryMediaUri}")
        viewModel.loadMedia()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddMediaBinding.inflate(inflater, container, false)

        // Once we've added a media, we set its URI to the currentMediaUri property.
        // Every time
        viewModel.currentMediaUri.observe(viewLifecycleOwner) { uri ->
            binding.mediaThumbnail.load(uri) {
                crossfade(true)
            }
        }

        binding.takePictureButton.setOnClickListener {
            viewModel.createPhotoUri(Source.CAMERA)?.let { uri ->
                actionTakePicture.launch(uri)
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
