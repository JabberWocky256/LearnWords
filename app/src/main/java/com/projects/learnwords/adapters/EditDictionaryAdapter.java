package com.projects.learnwords.adapters;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import com.projects.learnwords.activities.EditDictionaryActivity;
import com.projects.learnwords.app.DictionaryRowState;
import com.projects.learnwords.app.IDictionaryRow;
import com.projects.learnwords.app.IDictionaryRowStateDecorator;
import com.projects.learnwords.app.R;

import java.util.List;

/**
 * Created by Александр on 14.10.2014.
 */
public class EditDictionaryAdapter extends ArrayAdapter<IDictionaryRow> {
    private EditDictionaryActivity editDictionary;
    private final List<IDictionaryRow> values;

    public EditDictionaryAdapter(EditDictionaryActivity context, List<IDictionaryRow> values) {
        super(context, R.layout.file_manager_row, values);
        this.editDictionary = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        View view = null;
            if (convertView == null) {

                LayoutInflater inflator = editDictionary.getLayoutInflater();
                view = inflator.inflate(R.layout.edit_dictionary_rows, null);

                if(((IDictionaryRowStateDecorator)values.get(position)).getChangedState() !=
                        DictionaryRowState.DictionaryRowStates.REMOVE) {
                    TextView editWord = (TextView) view.findViewById(R.id.editFirstWord);
                    TextView editTranslate = (TextView) view.findViewById(R.id.editSecondWord);

                    editWord.setText(values.get(position).getFirstWord());
                    editTranslate.setText(values.get(position).getSecondWord());

                    editWord.setOnClickListener(wordListener(values.get(position), true, editWord));
                    editTranslate.setOnClickListener(wordListener(values.get(position), false, editTranslate));

                    editWord.setOnLongClickListener(rowListener(values.get(position)));
                    editTranslate.setOnLongClickListener(rowListener(values.get(position)));
                }

            } else {
                view = convertView;
            }

        return view;
    }

    private View.OnClickListener wordListener(final IDictionaryRow row, final boolean isFirstWord, final TextView word) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText newDictionaryName = new EditText(getContext());
                final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
                newDictionaryName.setText(word.getText().toString());

                DialogInterface.OnClickListener okButtonListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = newDictionaryName.getText().toString();
                        saveChanges(name, isFirstWord, row);
                        word.setText(name);
                        editDictionary.recreate();
                        editDictionary.setDictionaryChanged();
                    }
                };

                DialogInterface.OnClickListener cancelButtonListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                };

                alertBuilder
                        .setTitle("Редактирование")
                        .setMessage("Введите новое слово")
                        .setView(newDictionaryName)
                        .setPositiveButton("Ок", okButtonListener)
                        .setNegativeButton("Отмена", cancelButtonListener)
                        .show();
            }
        };
    }

    private void saveChanges(String name, boolean isFirstWord, IDictionaryRow row){
        ((IDictionaryRowStateDecorator)row).setChangedState(DictionaryRowState.DictionaryRowStates.UPDATE);
        if(isFirstWord)
            row.setFirstWord(name);
        else
            row.setSecondWord(name);
    }

    private View.OnLongClickListener rowListener(final IDictionaryRow row) {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                deleteRow(row);
                return true;
            }
        };
    }

    private void deleteRow(final IDictionaryRow row) {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());

        DialogInterface.OnClickListener okButtonListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((IDictionaryRowStateDecorator)row).setChangedState(DictionaryRowState.DictionaryRowStates.REMOVE);
                editDictionary.recreate();
                editDictionary.setDictionaryChanged();
            }
        };

        DialogInterface.OnClickListener cancelButtonListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        };

        alertBuilder
                .setTitle("Удаление")
                .setMessage("Удалить слово-перевод?")
                .setPositiveButton("Да", okButtonListener)
                .setNegativeButton("Отмена", cancelButtonListener)
                .show();
    }

}
