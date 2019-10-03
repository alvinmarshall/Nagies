package com.wNagiesEducationalCenterj_9905.ui.teacher.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation

import com.wNagiesEducationalCenterj_9905.R
import kotlinx.android.synthetic.main.fragment_file_management.*

class FileManagementFragment : Fragment() {
    private var explorerNav: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_file_management, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_assingment_pdf.setOnClickListener {
            explorerNav = getString(R.string.label_btn_assignment_pdf)
            setExplorer(explorerNav)
        }
        btn_assignment_image.setOnClickListener {
            explorerNav = getString(R.string.label_btn_assignment_image)
            setExplorer(explorerNav)
        }
        btn_report_pdf.setOnClickListener {
            explorerNav = getString(R.string.label_btn_report_pdf)
            setExplorer(explorerNav)
        }
        btn_report_image.setOnClickListener {
            explorerNav = getString(R.string.label_btn_report_image)
            setExplorer(explorerNav)
        }
    }

    private fun setExplorer(explorerNav: String?) {
        val action = FileManagementFragmentDirections.actionFileManagementFragmentToFileExplorerFragment(explorerNav)
        activity?.let { Navigation.findNavController(it, R.id.fragment_socket).navigate(action) }
    }

}
