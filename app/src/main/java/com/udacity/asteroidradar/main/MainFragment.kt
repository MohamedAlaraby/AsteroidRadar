package com.udacity.asteroidradar.main
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.domain.Filter

class MainFragment : Fragment() {
    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }
        ViewModelProvider(
            this,
            Factory(activity.application)
        ).get(MainViewModel::class.java)
    }
    lateinit var adapter: AsteroidsListAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
         adapter=AsteroidsListAdapter(onClickListener=AsteroidsListAdapter.OnClickListener{
            viewModel.displayAsteroidDetails(it)
        })
        binding.asteroidRecycler.adapter=adapter
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
            viewModel.onChangeFilter(
                when (item.itemId) {
                    R.id.show_today_asteroids_menu -> {
                       Filter.TODAY
                    }
                    R.id.show_next_week_menu -> {
                       Filter.NEXT_WEEK
                    }
                    else -> {
                        Filter.ALL
                    }
                }
            )
            return true
        }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.navigateToSelectedAsteroid.observe(viewLifecycleOwner, Observer { asteroid->
            asteroid?.let {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.displayPropertyDetailsComplete()
            }
        })
        viewModel.asteroidList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }
}
