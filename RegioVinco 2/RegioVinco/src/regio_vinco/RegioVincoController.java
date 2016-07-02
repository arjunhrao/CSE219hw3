package regio_vinco;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import pacg.KeyPressHook;

/**
 * This controller provides the appropriate responses for all interactions.
 */
public class RegioVincoController implements KeyPressHook {
    RegioVincoGame game;
    
    public RegioVincoController(RegioVincoGame initGame) {
	game = initGame;
    }
    
    public void processStartGameRequest() {
	try {
	    game.beginUsingData();
	    game.reset();
	}
	finally {
	    game.endUsingData();
	}
    }
    
    public void processExitGameRequest() {
	game.killApplication();
    }
    
    public void processMapClickRequest(int x, int y) {
	try {
	    game.beginUsingData();
	    ((RegioVincoDataModel)game.getDataModel()).respondToMapSelection(game, x, y);
	}
	finally {
	    game.endUsingData();
	}
    }
    
    @Override
    public void processKeyPressHook(KeyEvent ke)
    {
        KeyCode keyCode = ke.getCode();
        if (keyCode == KeyCode.C)
        {
            try
            {    
                game.beginUsingData();
                RegioVincoDataModel dataModel = (RegioVincoDataModel)(game.getDataModel());
                dataModel.removeAllButOneFromeStack(game);         
            }
            finally
            {
                game.endUsingData();
            }
        }
    }   
}
