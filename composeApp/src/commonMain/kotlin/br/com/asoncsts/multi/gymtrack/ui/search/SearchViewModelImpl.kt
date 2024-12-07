package br.com.asoncsts.multi.gymtrack.ui.search

import androidx.lifecycle.viewModelScope
import br.com.asoncsts.multi.gymtrack.data._utils.Wrapper
import br.com.asoncsts.multi.gymtrack.data.exercise.repository.ExerciseRepository
import br.com.asoncsts.multi.gymtrack.ui.search.SearchState.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SearchViewModelImpl(
    private val repo: ExerciseRepository
) : SearchViewModel() {

    private val _state = MutableStateFlow<SearchState>(Loading)
    override val state = _state.asStateFlow()

    override fun getExercises(
        force: Boolean
    ) {
        if (_state.value is Success && !force) return

        viewModelScope.launch {
            when (val result = repo.getExercises()) {
                is Wrapper.Error -> {
                    _state.update {
                        Error(
                            result.error
                        )
                    }
                }

                is Wrapper.Success -> {
                    _state.update {
                        Success(
                            result.data
                        )
                    }
                }
            }
        }
    }

}
