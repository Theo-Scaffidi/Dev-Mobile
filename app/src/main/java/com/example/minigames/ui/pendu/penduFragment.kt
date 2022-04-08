package com.example.minigames.ui.pendu

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.Log.VERBOSE
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.minigames.databinding.FragmentPenduBinding
import java.io.IOException
import java.io.InputStream


class penduFragment : Fragment() {
    //
    var word = ""
    val buttons = ArrayList<Button>()
    var fail = 0;
    //
    private var _binding: FragmentPenduBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel =
            ViewModelProvider(this).get(penduViewModel::class.java)
        _binding = FragmentPenduBinding.inflate(inflater, container, false)
        val root: View = binding.root
        //
        val buttonContainer = binding.buttonsContainer
        val allbuttons = buttonContainer.children //return Sequence<View>

        allbuttons.forEach { btn ->
            if(btn is Button){
                buttons.add(btn)
            }
        }
        //events
        binding.Reset.setOnClickListener({binding.Reset.visibility = View.INVISIBLE;start()})
        for (button in buttons){
            button.setOnClickListener { buttonClickHangMan(button) }
        }
        start()
        return root
    }

    private fun buttonClickHangMan(button: Button) {
        button.isEnabled = false
        button.isClickable = false
        playedLetter(button.text[0])
    }

    private fun playedLetter(c: Char) {
        var letterInWord = false
        var index = -1
        do {
            if(index!=-1){
                letterInWord=true;
                var findWord = binding.Word.text.toString().toCharArray()
                findWord[index*2] = c.lowercaseChar()
                binding.Word.text = String(findWord)
                isWin()
            }
            index = word.indexOf(c.lowercaseChar() , index + 1)
        }while (index != -1)
        if(!letterInWord){looseLife()}
    }

    private fun isWin() {
        if(!binding.Word.text.contains("_")){
            binding.Word.text="You win ! Retry ?"
            for (b in buttons) {
                b.isClickable = false;
            }
            resetButton()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun start(){
        init()
    }
    private fun looseLife() {
        when(fail){
            0->binding.HangMan.text="    \n   | \n   | \n   | \n   | \n   |\n   ------"
            1->binding.HangMan.text="    ----\n   |  \n   | \n   | \n   | \n   |\n   ------"
            2->binding.HangMan.text="    ----\n   |  |\n   |  \n   | \n   | \n   |\n   ------"
            3->binding.HangMan.text="    ----\n   |  |\n   |  O\n   | \n   |\n   |\n   ------"
            4->binding.HangMan.text="    ----\n   |  |\n   |  O\n   | /\n   | \n   |\n   ------"
            5->binding.HangMan.text="    ----\n   |  |\n   |  O\n   | /|\n   | \n   |\n   ------"
            6->binding.HangMan.text="    ----\n   |  |\n   |  O\n   | /|\\\n   | \n   |\n   ------"
            7->binding.HangMan.text="    ----\n   |  |\n   |  O\n   | /|\\\n   | / \n   |\n   ------"
            8-> {
                binding.HangMan.text = "    ----\n   |  |\n   |  O\n   | /|\\\n   | / \\\n   |\n   ------"
                gameOver()
            }
        }
        fail++
    }
    private fun resetButton() {
        binding.Reset.visibility = View.VISIBLE
    }
    private fun gameOver() {
        for (b in buttons) {
            b.isClickable = false;
        }
        binding.Word.text="You loose !"
        resetButton()
    }

    private fun init(){
        fail=0
        binding.HangMan.text=""
        loadWord()
        setButtonActivable()
    }

    private fun loadWord() {
        try {
            val inputStream: InputStream? = activity?.assets?.open("words.txt")
            if (inputStream != null) {
                word = inputStream.bufferedReader().readLines().random()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        binding.Word.text=""
        for(x in word.indices){
            binding.Word.text = binding.Word.text.toString()+"_ "
        }
    }

    private fun setButtonActivable() {
        for (button in buttons){
            button.isClickable = true;
            button.isEnabled = true;
        }
    }


}
