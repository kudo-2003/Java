/*
 * Copyright 2025 Oracle and/or its affiliates
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

package run_main.service;

import run_main.domain.Genre;
import run_main.repository.GenreRepository;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Singleton;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Singleton
public class GenreService {

    private final GenreRepository genreRepository;

    GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public Optional<Genre> findById(Long id) {
        return genreRepository.findById(id);
    }

    @Transactional
    public long update(long id, String name) {
        return genreRepository.update(id, name);
    }

    public List<Genre> list(Pageable pageable) {
        return genreRepository.findAll(pageable).getContent();
    }

    @Transactional
    public Genre save(String name) {
        return genreRepository.save(name);
    }

    @Transactional
    public void delete(long id) {
        genreRepository.deleteById(id);
    }
}
