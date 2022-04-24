package com.example.minigames.ui.morpion

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.minigames.MainActivity
import com.example.minigames.databinding.ActivityMainBinding
import com.example.minigames.databinding.FragmentMorpionBinding

class MorpionFragment : Fragment() {

    //private var _binding: FragmentMorpionBinding? = null
    private lateinit var _binding: FragmentMorpionBinding

    enum class Turn{
        NOUGHT,
        CROSS
    }
    private var firstTurn = Turn.CROSS
    private var currentTurn = Turn.CROSS

    private var boardList = mutableListOf<Button>()
    // This property is only valid between onCreateView and
    // onDestroyView.
    //private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val morpionViewModel =
            ViewModelProvider(this).get(MorpionViewModel::class.java)

        _binding = FragmentMorpionBinding.inflate(inflater, container, false)
        val root: View = _binding.root
        initBoard()

        val textView: TextView = _binding.playerTurn
        morpionViewModel.text.observe(viewLifecycleOwner) {
            setTurnLabel()
        }
        for (button in boardList){
            button.setOnClickListener { boardTapped(button) }
        }
        return root
    }

    /*override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }*/

    private fun initBoard() {
        boardList.add(_binding.button2)
        boardList.add(_binding.button3)
        boardList.add(_binding.button4)
        boardList.add(_binding.button5)
        boardList.add(_binding.button6)
        boardList.add(_binding.button7)
        boardList.add(_binding.button8)
        boardList.add(_binding.button9)
        boardList.add(_binding.button10)
    }
    private fun boardTapped(view: View){
        if(view !is Button)
            return
        addToBoard(view)

        if(checkForVictory(NOUGHT)){
            result("Joueur 1 Gagne")
        }
        if(checkForVictory(CROSS)){
            result("Joueur 2 Gagne")
        }

        if(fullBoard() && !checkForVictory(CROSS) && !checkForVictory(NOUGHT)){
            result("Egalité")
        }
    }

    private fun checkForVictory(s: String): Boolean {
        if(match(_binding.button2,s) && match(_binding.button3,s) && match(_binding.button4,s)){
            return true
        }
        if(match(_binding.button5,s) && match(_binding.button6,s) && match(_binding.button7,s)){
            return true
        }
        if(match(_binding.button8,s) && match(_binding.button9,s) && match(_binding.button10,s)){
            return true
        }

        if(match(_binding.button2,s) && match(_binding.button5,s) && match(_binding.button8,s)){
            return true
        }
        if(match(_binding.button3,s) && match(_binding.button6,s) && match(_binding.button9,s)){
            return true
        }
        if(match(_binding.button4,s) && match(_binding.button7,s) && match(_binding.button10,s)){
            return true
        }

        if(match(_binding.button2,s) && match(_binding.button6,s) && match(_binding.button10,s)){
            return true
        }
        if(match(_binding.button4,s) && match(_binding.button6,s) && match(_binding.button8,s)){
            return true
        }
        return false
    }

    private fun match(button: Button, symbol : String): Boolean = button.text == symbol

    private fun result(title: String) {
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setPositiveButton("Relancer")
            { _,_ ->
                resetBoard()
            }
            .setCancelable(false)
            .show()
    }

    private fun resetBoard() {
        for(button in boardList){
            button.text = ""
        }

        if(firstTurn ==  Turn.NOUGHT){
            firstTurn = Turn.CROSS
        }else if(firstTurn ==  Turn.CROSS){
            firstTurn = Turn.NOUGHT
        }
        currentTurn = firstTurn
        setTurnLabel()
    }

    private fun fullBoard(): Boolean {
        for(button in boardList){
            if(button.text == ""){
                return false
            }
        }
        return true
    }

    private fun addToBoard(button: Button) {
        if(button.text != "")
            return

        if(currentTurn == Turn.NOUGHT){
            button.text = NOUGHT
            currentTurn = Turn.CROSS
        }
        else if(currentTurn == Turn.CROSS){
            button.text = CROSS
            currentTurn = Turn.NOUGHT
        }
        setTurnLabel()
    }

    private fun setTurnLabel() {
        var turnText = ""
        if(currentTurn == Turn.CROSS)
            turnText = "Tour du Joueur 2"
        else if(currentTurn == Turn.NOUGHT)
            turnText = "Tour du Joueur 1"

        _binding.playerTurn.text = turnText
    }

    companion object{
        const val NOUGHT = "O"
        const val CROSS = "X"
    }
}