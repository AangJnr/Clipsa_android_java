package com.clipsa.utilities;

import android.view.View;

import com.cowtribe.cowtribeapps.data.db.entity.Question;
import com.cowtribe.cowtribeapps.data.db.entity.SkipLogic;
import com.cowtribe.cowtribeapps.ui.form.MyFormController;
import com.cowtribe.cowtribeapps.ui.form.model.FormModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import static com.cowtribe.cowtribeapps.ui.base.BaseActivity.getGson;

public class ComputationUtils {

    private MyFormController formController;
    private ScriptEngine engine;



    public static ComputationUtils newInstance(MyFormController myFormController){
        return new ComputationUtils(myFormController);

    }


   public ComputationUtils(MyFormController controller){

        engine = new ScriptEngineManager().getEngineByName("rhino");
        this.formController = controller;

    }



    FormModel getModel(){return formController.getModel();}


    public MyFormController getFormController() {
        return formController;
    }

    public boolean validate() throws JSONException {
        formController.resetValidationErrors();
        if (formController.isValidInput()) {

            //Send data to server here after getting JSON string

            //Toast.makeText(getContext(), getAllAnswersInJSON(), Toast.LENGTH_LONG).show();


        } else {

            // Whoaaaaaaa! There were some invalid inputs
            formController.showValidationErrors();
        }
        return true;
    }

    public String getAllAnswersAsJsonString(List<Question> QUESTIONS) {

        JSONObject jsonObject = new JSONObject();

        for (Question q : QUESTIONS) {
            try {
                jsonObject.put(String.valueOf(q.getId()), getModel().getValue(String.valueOf(q.getId())));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return jsonObject.toString();


    }

    public JSONObject getAllAnswersInJSONObject(List<Question> QUESTIONS) {


        JSONObject jsonObject = new JSONObject();

        for (Question q : QUESTIONS) {
            try {
                jsonObject.put(String.valueOf(q.getId()), getModel().getValue(String.valueOf(q.getId())));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return jsonObject;


    }



    public String getValue(Question q, JSONObject ANSWERS_JSON) {
        String defVal;
        try {

            if (ANSWERS_JSON.has(String.valueOf(q.getId()))) {
                defVal = ANSWERS_JSON.get(String.valueOf(q.getId())).toString();
                getModel().setValue(String.valueOf(q.getId()), defVal);
            } else
                defVal = "";


        } catch (JSONException ignored) {
            defVal = "";
        }
        AppLogger.i(getClass().getSimpleName(), "GETTING VALUE FOR " + q.getName() + " --> Value = " + defVal);


        return defVal;


    }



    public String getValueSpecials(Question question, JSONObject ANSWERS_JSON) {
        String defVal;
        try {

            if (ANSWERS_JSON.has(question.getName())) {
                defVal = ANSWERS_JSON.getString(question.getName());
                //getModel().setValue(String.valueOf(question.getId()), defVal);
            } else
                defVal = "";


        } catch (JSONException ignored) {
            defVal = "";
        }
        AppLogger.i(getClass().getSimpleName(), "GETTING VALUE FOR " + question.getName() + " --> Value = " + defVal);


        return defVal;
    }


    public void setUpPropertyChangeListenersByName(String label, List<SkipLogic> skipLogics) {

        if (skipLogics != null && skipLogics.size() > 0) {
           getModel().addPropertyChangeListener(label, event -> {

                AppLogger.e("PROPERTY CHANGE ", " FOR QUESTION " + label + " -----  Value was: " + event.getOldValue() + ", now: " + event.getNewValue());

                for (SkipLogic sl : skipLogics) {

                    try {
                        if (compareValues(sl, String.valueOf(event.getNewValue()))) {

                            if (sl.shouldHide())
                                getFormController().getElement(sl.getComparingQuestionName()).getView().setVisibility(View.GONE);

                            else
                                getFormController().getElement(sl.getComparingQuestionName()).getView().setVisibility(View.VISIBLE);

                        } else {

                            if (sl.shouldHide())
                                getFormController().getElement(sl.getComparingQuestionName()).getView().setVisibility(View.VISIBLE);
                            else
                                getFormController().getElement(sl.getComparingQuestionName()).getView().setVisibility(View.GONE);
                        }
                    } catch (Exception ignored) {
                    }
                }
            });


        }
    }


    public void initiateSkipLogicsAndHideViewsByName(String label, List<SkipLogic> skipLogics) {

            if (skipLogics != null && skipLogics.size() > 0) {

                for (final SkipLogic sl : skipLogics) {

                    try {

                        AppLogger.i("SKIP LOGIC view to hide = ", label);

                        if (compareValues(sl, formController.getModel().getValue(label).toString())) {

                            AppLogger.i(getClass().getSimpleName(), "COMPARING VALUES EVALUATED TO " + true);

                            if (sl.shouldHide())
                                formController.getElement(sl.getComparingQuestionName()).getView().setVisibility(View.GONE);
                            else
                                formController.getElement(sl.getComparingQuestionName()).getView().setVisibility(View.VISIBLE);

                        } else {

                            AppLogger.i(getClass().getSimpleName(), "COMPARING VALUES EVALUATED TO " + false);

                            if (sl.shouldHide())
                                formController.getElement(sl.getComparingQuestionName()).getView().setVisibility(View.VISIBLE);
                            else formController.getElement(sl.getComparingQuestionName()).getView().setVisibility(View.GONE);

                        }


                    } catch (Exception ignored) {
                    }

                }
            }



    }






    public void setUpPropertyChangeListeners(String label, List<SkipLogic> skipLogics) {

        if (skipLogics != null && skipLogics.size() > 0) {
            getModel().addPropertyChangeListener(label, event -> {

                AppLogger.e("PROPERTY CHANGE ", " FOR QUESTION " + label + " -----  Value was: " + event.getOldValue() + ", now: " + event.getNewValue());

                for (SkipLogic sl : skipLogics) {

                    try {
                        if (compareValues(sl, String.valueOf(event.getNewValue()))) {


                            if (sl.shouldHide()) {
                                AppLogger.e("ComputationUtils", "HIDING QUESTION WITH ID " + sl.getComparingQuestionId());
                                getFormController().getElement(String.valueOf(sl.getComparingQuestionId())).getView().setVisibility(View.GONE);

                            }

                            else {
                                AppLogger.e("ComputationUtils", "SHOWING QUESTION WITH ID " + sl.getComparingQuestionId());
                                getFormController().getElement(String.valueOf(sl.getComparingQuestionId())).getView().setVisibility(View.VISIBLE);
                            }

                        } else {

                            if (sl.shouldHide()) {
                                AppLogger.e("ComputationUtils", "INVERSELY SHOWING QUESTION WITH ID " + sl.getComparingQuestionId());

                                getFormController().getElement(String.valueOf(sl.getComparingQuestionId())).getView().setVisibility(View.VISIBLE);

                            }
                            else {
                                AppLogger.e("ComputationUtils", "INVERSELY HIDING QUESTION WITH ID " + sl.getComparingQuestionId());


                                getFormController().getElement(String.valueOf(sl.getComparingQuestionId())).getView().setVisibility(View.GONE);

                            }
                        }
                    } catch (Exception ignored) {

                    }
                }
            });


        }
    }



    public void setCheckboxListeners(String label, List<SkipLogic> skipLogics) {

        if (skipLogics != null && skipLogics.size() > 0) {
            getModel().addPropertyChangeListener(label, event -> {

                AppLogger.e("PROPERTY CHANGE ", " FOR QUESTION " + label + " -----  Value was: " + event.getOldValue() + ", now: " + event.getNewValue());

                for (SkipLogic sl : skipLogics) {

                    try {
                        if (compareValues(sl, String.valueOf(event.getNewValue()))) {


                            if (sl.shouldHide()) {
                                AppLogger.e("ComputationUtils", "HIDING QUESTION WITH ID " + sl.getComparingQuestionId());
                                getFormController().getElement(String.valueOf(sl.getComparingQuestionId())).getView().setVisibility(View.GONE);

                            }

                            else {
                                AppLogger.e("ComputationUtils", "SHOWING QUESTION WITH ID " + sl.getComparingQuestionId());
                                getFormController().getElement(String.valueOf(sl.getComparingQuestionId())).getView().setVisibility(View.VISIBLE);
                            }


                        }
                    } catch (Exception ignored) {

                    }
                }
            });


        }
    }


    public void initiateSkipLogicsAndHideViews(String label, List<SkipLogic> skipLogics) {

        if (skipLogics != null && skipLogics.size() > 0) {

            for (final SkipLogic sl : skipLogics) {

                try {

                    AppLogger.i("SKIP LOGIC view to hide = ", sl.getComparingQuestionId());

                    if (compareValues(sl, formController.getModel().getValue(label).toString())) {

                        AppLogger.i(getClass().getSimpleName(), "COMPARING VALUES EVALUATED TO " + true);

                        if (sl.shouldHide())
                            formController.getElement(String.valueOf(sl.getComparingQuestionId())).getView().setVisibility(View.GONE);
                        else
                            formController.getElement(String.valueOf(sl.getComparingQuestionId())).getView().setVisibility(View.VISIBLE);

                    } /*else {

                        AppLogger.i(getClass().getSimpleName(), "COMPARING VALUES EVALUATED TO " + false);

                        if (sl.shouldHide())
                            formController.getElement(String.valueOf(sl.getComparingQuestionId())).getView().setVisibility(View.VISIBLE);
                        else formController.getElement(String.valueOf(sl.getComparingQuestionId())).getView().setVisibility(View.GONE);

                    }*/


                } catch (Exception ignored) {
                }

            }
        }



    }





    public void setUpPropertyChangeListeners(Question question) {

        if(question.getOptions() != null){

            AppLogger.e("COMPUTATION PROPERTY CHANGE ", " OPTIONS " + question.getOptions());


           String[] ids;
           String[] results = question.getOptions().split("[-+*/]");

            ids = results;

            AppLogger.e("COMPUTATION PROPERTY CHANGE ", " OPTIONS SIZE = " + ids.length);


            for(String s : results){
                getModel().addPropertyChangeListener(s, event -> {

                    AppLogger.e("COMPUTATION PROPERTY CHANGE ", " FOR QUESTION " + question.getLabel());
                    AppLogger.e("COMPUTATION PROPERTY CHANGE ", " OPTIONS = " + getGson().toJson(ids));

                        try {

                            String equation = question.getOptions();


                            for(String id : ids)
                                equation = equation.replace(id, (String) getModel().getValue(id));


                            AppLogger.e("EQUATION AFTER REPLACEMENT ", equation);


                            String value = calculate(equation);
                            AppLogger.e("VALUE >>>",  value);

                            AppLogger.e("SETTING >>>", question.getId());


                            getModel().setValue(String.valueOf(question.getId()), value);

                        } catch (Exception ignored) {

                    }
                });




            }
        }

    }









    String calculate(String equation) throws ScriptException {

        AppLogger.e("EQUATION IS >>>",  equation);
        double value = (double) engine.eval(equation.trim());
        return String.valueOf((int) value);
    }

/*


    String calculate(String equation) throws ScriptException {

        AppLogger.e("EQUATION IS >>>",  equation);


        Double value = (Double) engine.eval(equation.trim());
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        DecimalFormat formatter = (DecimalFormat) nf;
        formatter.applyPattern("#,###,###.##");
        return (formatter.format(value));
    }
*/

    Boolean compareValues(SkipLogic sl, String newValue) {

        String equation = sl.getValue() + sl.getOperator() + String.valueOf(newValue);

        AppLogger.i(getClass().getSimpleName(), "Equation is " + equation);

        boolean value = false;
        try {
            value = (Boolean) engine.eval(equation.trim());

        } catch (ScriptException | NumberFormatException e) {
            System.out.println("******* EXCEPTION ****** " + e.getMessage());

                value = (sl.getOperator().equalsIgnoreCase("contains") ? newValue.contains(sl.getValue()) : sl.getValue().equalsIgnoreCase(newValue));


        } finally {
            System.out.println(equation + " --> " + value);
        }
        return value;
    }




    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }





}
