package com.example.beerproject.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beerproject.R
import com.example.beerproject.model.Beer
import com.example.beerproject.network.Status
import com.example.beerproject.viewmodels.BeerListViewModel
import kotlinx.android.synthetic.main.beer_list.*

class BeerListFragment : Fragment() {
    private val beersViewModel: BeerListViewModel by activityViewModels()
    private lateinit var layoutManager: LinearLayoutManager
    private var statusView: View? = null
    private var beerItemViewAdapter: BeerItemViewAdapter? = null

    private var items: MutableList<Beer> = mutableListOf()

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var isViewTwoPane: Boolean = false

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initSearchListener()
    }

    /** SearchView btn action */
    @SuppressLint("NotifyDataSetChanged")
    private fun initSearchListener() {
        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null && query.isNotEmpty()) {

                    beersViewModel
                        .searchBeers(query.trim())
                        .observe(viewLifecycleOwner) { beers ->
                            beersViewModel.setBeerList(beers)
                        }

                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        //Observe live data changes and update UI accordingly
        beersViewModel.beers.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    Log.d("GET BEERS", "LOADING...")
                    txtError.visibility = View.GONE
                    pbRepoListLoading.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    Log.d("GET BEERS", "SUCCESS")
                    if (beerItemViewAdapter != null) {
                        items.clear()
                        items.addAll(it.data!!)
                        beerItemViewAdapter?.notifyDataSetChanged()
                    } else {
                        items.addAll(it.data!!)
                        setupRecyclerView(recycler_beerlist)
                    }
                    pbRepoListLoading.visibility = View.GONE
                }
                Status.ERROR -> {
                    Log.d("GET BEERS", "ERROR")
                    pbRepoListLoading.visibility = View.GONE
                    txtError.visibility = View.VISIBLE
                    txtError.text = it.message
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /*NOTE: On complex view hierarchies, it's a potential memory leak to store views in memory.
        However, as Android new Navigation system destroys and recreate fragment views on every
        navigation and extra info such as scroll is lost, I'm storing it in memory for now given that
        app view hierarchy is simple.
         */
        if (statusView == null) {
            statusView = inflater.inflate(R.layout.beer_list, container, false)
        }

        if (statusView?.findViewById<FrameLayout>(R.id.beer_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            isViewTwoPane = true
        }

        return statusView
    }

    /**
     * Configures beer list recycler view
     */
    private fun setupRecyclerView(recyclerView: RecyclerView) {
        layoutManager = LinearLayoutManager(activity)
        val dividerItemDecoration = DividerItemDecoration(
            recycler_beerlist.context,
            layoutManager.orientation
        )
        recycler_beerlist.addItemDecoration(dividerItemDecoration)
        recycler_beerlist.layoutManager = layoutManager

        beerItemViewAdapter = BeerItemViewAdapter(
            activity as MainActivity,
            items,
            isViewTwoPane
        )

        recyclerView.adapter = beerItemViewAdapter

    }
}