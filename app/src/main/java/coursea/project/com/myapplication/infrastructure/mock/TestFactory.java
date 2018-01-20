package coursea.project.com.myapplication.infrastructure.mock;

import java.util.ArrayList;
import java.util.List;

import coursea.project.com.myapplication.domain.model.QuestionWithAnswers;
import coursea.project.com.myapplication.domain.model.Test;

import static coursea.project.com.myapplication.infrastructure.mock.QuestionWithAnswersFactory.createQuestionWithAnswers;

/**
 * Created by chidra on 12/23/17.
 */

public class TestFactory {



    public static Test createHistoryTest1(){

        List<QuestionWithAnswers> questionWithAnswers = new ArrayList<>();
        final String TEST1_NAME = "History Napoleaon";
        final String TEST1_DESC="La Revolution Francaise";

        final String   TEST1_Q_1 = "D'ou s'est evade Napoleon";
        final String[] TEST1_A_1A ={"De null part","false"};
        final String[] TEST1_A_1B ={"De Pologne","false"};
        final String[] TEST1_A_1C ={"D' Algerie","false"};
        final String[] TEST1_A_1D ={"De l'ile d'Elbe","true"};


        final String   TEST1_Q_2 = "De quelle couleur est le cheval blanc d'Henry IV";
        final String[] TEST1_A_2A ={"Blue","false"};
        final String[] TEST1_A_2B ={"Rouge","false"};
        final String[] TEST1_A_2C ={"Vert","false"};
        final String[] TEST1_A_2D ={"Blanc","true"};


        final String   TEST1_Q_3 = "Qui est le plus beau";
        final String[] TEST1_A_3A ={"Papa","false"};
        final String[] TEST1_A_3B ={"Papa","false"};
        final String[] TEST1_A_3C ={"Rachid","false"};
        final String[] TEST1_A_3D ={"Chidra","true"};


        final String   TEST1_Q_4 = "Quelle est l'equipe qui a gagne le plus de Champions League";
        final String[] TEST1_A_4A ={"Paris S.G","false"};
        final String[] TEST1_A_4B ={"Real Madrid","true"};
        final String[] TEST1_A_4C ={"Barcelone","false"};
        final String[] TEST1_A_4D ={"Bayern Munich","false"};

        questionWithAnswers.add(createQuestionWithAnswers(TEST1_Q_1,TEST1_A_1A, TEST1_A_1B, TEST1_A_1C, TEST1_A_1D));
        questionWithAnswers.add(createQuestionWithAnswers(TEST1_Q_2,TEST1_A_2A, TEST1_A_2B, TEST1_A_2C, TEST1_A_2D));
        questionWithAnswers.add(createQuestionWithAnswers(TEST1_Q_3,TEST1_A_3A, TEST1_A_3B, TEST1_A_3C, TEST1_A_3D));
        questionWithAnswers.add(createQuestionWithAnswers(TEST1_Q_4,TEST1_A_4A, TEST1_A_4B, TEST1_A_4C, TEST1_A_4D));


        Test test = new Test(TEST1_NAME, TEST1_DESC,questionWithAnswers,0);

        return  test;

    }



}
