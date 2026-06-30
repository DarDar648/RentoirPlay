package com.app.tiketin.v1

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.tiketin.v1.adapter.WisataAdapter
import com.app.tiketin.v1.data.TiketinRepository
import com.app.tiketin.v1.databinding.FragmentHomeBinding
import com.app.tiketin.v1.model.WisataItem

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: UserSessionManager
    private lateinit var wisataAdapter: WisataAdapter
    private var allWisata = listOf<WisataItem>()
    private var isGridView = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        sessionManager = UserSessionManager(requireContext())
        
        setupHeader()
        setupRecyclerView()
        setupSearch()
        
        binding.incSearch.btnFilter.setOnClickListener {
            toggleLayout()
        }

        binding.btnLogout.setOnClickListener {
            sessionManager.logout()
            Toast.makeText(requireContext(), "Logout berhasil!", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            activity?.finish()
        }

        loadWisata()
        
        // Sync Ragunan dan Ancol
        TiketinRepository.syncRagunanToFirebase { success ->
            if (success) loadWisata()
        }
    }

    private fun loadWisata() {
        TiketinRepository.getWisata(
            onSuccess = { list ->
                allWisata = list
                wisataAdapter.updateData(list)
            },
            onError = {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun setupHeader() {
        val username = sessionManager.getUsername()
        binding.incHeader.tvGreetingName.text = "$username!"
    }

    private fun setupRecyclerView() {
        wisataAdapter = WisataAdapter(allWisata) { wisata ->
            val intent = Intent(requireContext(), WisataDetailActivity::class.java)
            intent.putExtra(WisataDetailActivity.EXTRA_ID, wisata.id)
            startActivity(intent)
        }

        binding.rvWisata.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = wisataAdapter
        }
    }

    private fun setupSearch() {
        binding.incSearch.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterData(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun toggleLayout() {
        isGridView = !isGridView
        wisataAdapter.setGridView(isGridView)
        binding.rvWisata.layoutManager = if (isGridView) {
            GridLayoutManager(requireContext(), 2)
        } else {
            LinearLayoutManager(requireContext())
        }
    }

    private fun filterData(query: String) {
        val filteredList = if (query.isEmpty()) {
            allWisata
        } else {
            allWisata.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.location.contains(query, ignoreCase = true)
            }
        }
        wisataAdapter.updateData(filteredList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}