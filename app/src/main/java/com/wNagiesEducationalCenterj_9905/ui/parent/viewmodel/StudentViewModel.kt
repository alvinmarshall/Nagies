package com.wNagiesEducationalCenterj_9905.ui.parent.viewmodel

import com.wNagiesEducationalCenterj_9905.data.repository.StudentRepository
import com.wNagiesEducationalCenterj_9905.viewmodel.BaseViewModel
import javax.inject.Inject

class StudentViewModel @Inject constructor(private val studentRepository: StudentRepository) : BaseViewModel() {

}