package com.parrot.freeflight.ui.controlhandlers;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.parrot.freeflight.settings.ApplicationSettings;
import com.parrot.freeflight.settings.ApplicationSettings.ControlMode;
import com.parrot.freeflight.ui.HudViewController;
import com.parrot.freeflight.ui.HudViewController.JoystickType;
import com.parrot.freeflight.ui.hud.AcceleroJoystick;
import com.parrot.freeflight.ui.hud.AnalogueJoystick;
import com.parrot.freeflight.ui.hud.JoystickBase;
import com.parrot.freeflight.ui.hud.JoystickFactory;
import com.parrot.freeflight.ui.hud.JoystickListener;
import com.parrot.freeflight.utils.OnPressListener;

public class InputEventsHandlerDefault implements InputEventsHandler {
    private static final String TAG = "InputEventsHandlerFactoryDefault";
    static final int TRESHOLD_DOUBLE_TAP = 500;
    protected SparseArray<Action> buttonsMapping;
    protected OnDroneFlipListener droneFlipListener;
    private JoystickListener gazYawJoypadListener;
    protected OnGazYawChangedListener gazYawListener;
    private int inputSourceOrientation;
    private long lastFlipClick;
    protected OnRollPitchChangedListener pitchRollListener;
    private JoystickListener rollPitchJoypadListener;
    protected ApplicationSettings settings;
    protected OnUiActionsListener uiActionsListener;
    protected HudViewController view;

    class C12291 extends JoystickListener {
        C12291() {
        }

        public void onChanged(JoystickBase joystickBase, float f, float f2) {
            if (InputEventsHandlerDefault.this.gazYawListener != null) {
                InputEventsHandlerDefault.this.gazYawListener.onGazYawChanged(f2, f);
            }
        }

        public void onPressed(JoystickBase joystickBase) {
            if (InputEventsHandlerDefault.this.gazYawListener != null) {
                InputEventsHandlerDefault.this.gazYawListener.onGazYawActivated();
            }
        }

        public void onReleased(JoystickBase joystickBase) {
            if (InputEventsHandlerDefault.this.gazYawListener != null) {
                InputEventsHandlerDefault.this.gazYawListener.onGazYawDeactivated();
            }
        }
    }

    class C12302 extends JoystickListener {
        C12302() {
        }

        public void onChanged(JoystickBase joystickBase, float f, float f2) {
            if (InputEventsHandlerDefault.this.pitchRollListener != null) {
                InputEventsHandlerDefault.this.pitchRollListener.onRollPitchChanged(f2, f);
            }
        }

        public void onPressed(JoystickBase joystickBase) {
            if (InputEventsHandlerDefault.this.pitchRollListener != null) {
                InputEventsHandlerDefault.this.pitchRollListener.onRollPitchActivated();
            }
        }

        public void onReleased(JoystickBase joystickBase) {
            if (InputEventsHandlerDefault.this.pitchRollListener != null) {
                InputEventsHandlerDefault.this.pitchRollListener.onRollPitchDeactivated();
            }
        }
    }

    class C12313 implements OnClickListener {
        C12313() {
        }

        public void onClick(View view) {
            if (InputEventsHandlerDefault.this.uiActionsListener != null) {
                InputEventsHandlerDefault.this.uiActionsListener.onSettingsClicked();
            }
        }
    }

    class C12324 implements OnClickListener {
        C12324() {
        }

        public void onClick(View view) {
            if (InputEventsHandlerDefault.this.uiActionsListener != null) {
                InputEventsHandlerDefault.this.uiActionsListener.onCameraSwitchClicked();
            }
        }
    }

    class C12335 implements OnClickListener {
        C12335() {
        }

        public void onClick(View view) {
            if (InputEventsHandlerDefault.this.uiActionsListener != null) {
                InputEventsHandlerDefault.this.uiActionsListener.onTakeOffLandClicked();
            }
        }
    }

    class C12346 implements OnClickListener {
        C12346() {
        }

        public void onClick(View view) {
            if (InputEventsHandlerDefault.this.uiActionsListener != null) {
                InputEventsHandlerDefault.this.uiActionsListener.onBackToHomeClicked();
            }
        }
    }

    class C12357 implements OnClickListener {
        C12357() {
        }

        public void onClick(View view) {
            if (InputEventsHandlerDefault.this.uiActionsListener != null) {
                InputEventsHandlerDefault.this.uiActionsListener.onRescueClicked();
            }
        }
    }

    class C12368 implements OnClickListener {
        C12368() {
        }

        public void onClick(View view) {
            if (InputEventsHandlerDefault.this.uiActionsListener != null) {
                InputEventsHandlerDefault.this.uiActionsListener.onMapGoClicked();
            }
        }
    }

    class C12379 implements OnPressListener {
        C12379() {
        }

        public void onClick(View view) {
            if (InputEventsHandlerDefault.this.uiActionsListener != null) {
                InputEventsHandlerDefault.this.uiActionsListener.onMapLeftPressed();
            }
        }

        public void onRelease(View view) {
            if (InputEventsHandlerDefault.this.uiActionsListener != null) {
                InputEventsHandlerDefault.this.uiActionsListener.onMapLeftReleased();
            }
        }

        public void onRepeated(View view) {
            if (InputEventsHandlerDefault.this.uiActionsListener != null) {
                InputEventsHandlerDefault.this.uiActionsListener.onMapLeftRepeated();
            }
        }
    }

    protected enum Action {
        UP,
        DOWN,
        TURN_LEFT,
        TURN_RIGHT,
        ACCELERO,
        TAKE_OFF,
        EMERGENCY,
        CAMERA,
        SETTINGS,
        FLIP,
        VIDEO_RECORD,
        PHOTO
    }

    InputEventsHandlerDefault(Activity activity, HudViewController hudViewController, ApplicationSettings applicationSettings) {
        this.view = hudViewController;
        this.settings = applicationSettings;
        initListeners();
        onInitOnScreenJoysticks(applicationSettings.getControlMode(), applicationSettings.isLeftHanded());
        this.inputSourceOrientation = activity.getWindow().getWindowManager().getDefaultDisplay().getRotation();
    }

    public int getInputSourceOrientation() {
        return this.inputSourceOrientation;
    }

    protected void initListeners() {
        this.gazYawJoypadListener = new C12291();
        this.rollPitchJoypadListener = new C12302();
        this.view.setSettingsButtonClickListener(new C12313());
        this.view.setBtnCameraSwitchClickListener(new C12324());
        this.view.setBtnTakeOffClickListener(new C12335());
        this.view.setBtnBackToHomeClickListener(new C12346());
        this.view.setBtnRescueClickListener(new C12357());
        this.view.setBtnMapGoClickListener(new C12368());
        this.view.setBtnMapLeftClickListener(null, new C12379());
        this.view.setBtnMapRightClickListener(null, new OnPressListener() {
            public void onClick(View view) {
                if (InputEventsHandlerDefault.this.uiActionsListener != null) {
                    InputEventsHandlerDefault.this.uiActionsListener.onMapRightPressed();
                }
            }

            public void onRelease(View view) {
                if (InputEventsHandlerDefault.this.uiActionsListener != null) {
                    InputEventsHandlerDefault.this.uiActionsListener.onMapRightReleased();
                }
            }

            public void onRepeated(View view) {
                if (InputEventsHandlerDefault.this.uiActionsListener != null) {
                    InputEventsHandlerDefault.this.uiActionsListener.onMapRightRepeated();
                }
            }
        });
        this.view.setBtnRandomShakeClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (InputEventsHandlerDefault.this.uiActionsListener != null) {
                    InputEventsHandlerDefault.this.uiActionsListener.onRandomShakeClicked();
                }
            }
        });
        this.view.setBtnOverBalanceClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (InputEventsHandlerDefault.this.uiActionsListener != null) {
                    InputEventsHandlerDefault.this.uiActionsListener.onOverBalanceClicked();
                }
            }
        });
        this.view.setBtnEmergencyClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (InputEventsHandlerDefault.this.uiActionsListener != null) {
                    InputEventsHandlerDefault.this.uiActionsListener.onEmergencyClicked();
                }
            }
        });
        this.view.setBtnPhotoClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (InputEventsHandlerDefault.this.uiActionsListener != null) {
                    InputEventsHandlerDefault.this.uiActionsListener.onPhotoTakeClicked();
                }
            }
        });
        this.view.setBtnRecordClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (InputEventsHandlerDefault.this.uiActionsListener != null) {
                    InputEventsHandlerDefault.this.uiActionsListener.onVideoRecordClicked();
                }
            }
        });
        this.view.setBtnGpsClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (InputEventsHandlerDefault.this.uiActionsListener != null) {
                    InputEventsHandlerDefault.this.uiActionsListener.onGpsClicked();
                }
            }
        });
        this.view.setBtnBackClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (InputEventsHandlerDefault.this.uiActionsListener != null) {
                    InputEventsHandlerDefault.this.uiActionsListener.onBackClicked();
                }
            }
        });
        this.view.setDoubleTapClickListener(new OnDoubleTapListener() {
            public boolean onDoubleTap(MotionEvent motionEvent) {
                return (InputEventsHandlerDefault.this.droneFlipListener == null || !InputEventsHandlerDefault.this.view.isMotionEventInWorkArea(motionEvent)) ? false : InputEventsHandlerDefault.this.droneFlipListener.onFlip();
            }

            public boolean onDoubleTapEvent(MotionEvent motionEvent) {
                return false;
            }

            public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
                return false;
            }
        });
    }

    protected void initVirtualJoysticks(JoystickType joystickType, JoystickType joystickType2, boolean z) {
        Context context = this.view.getRootView().getContext();
        JoystickBase joystickLeft = !z ? this.view.getJoystickLeft() : this.view.getJoystickRight();
        JoystickBase joystickRight = !z ? this.view.getJoystickRight() : this.view.getJoystickLeft();
        if (joystickType == JoystickType.ANALOGUE) {
            if (joystickLeft != null && (joystickLeft instanceof AnalogueJoystick) && joystickLeft.isAbsoluteControl() == this.settings.isAbsoluteControlEnabled()) {
                joystickLeft.setOnAnalogueChangedListener(this.rollPitchJoypadListener);
                joystickLeft.setAbsolute(this.settings.isAbsoluteControlEnabled());
            } else {
                joystickLeft = JoystickFactory.createAnalogueJoystick(context, this.settings.isAbsoluteControlEnabled(), this.rollPitchJoypadListener);
            }
        } else if (joystickType == JoystickType.ACCELERO) {
            if (joystickLeft != null && (joystickLeft instanceof AcceleroJoystick) && joystickLeft.isAbsoluteControl() == this.settings.isAbsoluteControlEnabled()) {
                joystickLeft.setOnAnalogueChangedListener(this.rollPitchJoypadListener);
                joystickLeft.setAbsolute(this.settings.isAbsoluteControlEnabled());
            } else {
                joystickLeft = JoystickFactory.createAcceleroJoystick(context, this.settings.isAbsoluteControlEnabled(), this.rollPitchJoypadListener);
            }
        }
        if (joystickType2 == JoystickType.ANALOGUE) {
            if (joystickRight != null && (joystickRight instanceof AnalogueJoystick) && joystickRight.isAbsoluteControl() == this.settings.isAbsoluteControlEnabled()) {
                joystickRight.setOnAnalogueChangedListener(this.gazYawJoypadListener);
                joystickRight.setAbsolute(false);
            } else {
                joystickRight = JoystickFactory.createAnalogueJoystick(context, false, this.gazYawJoypadListener);
            }
        } else if (joystickType2 == JoystickType.ACCELERO) {
            if (joystickRight != null && (joystickRight instanceof AcceleroJoystick) && joystickRight.isAbsoluteControl() == this.settings.isAbsoluteControlEnabled()) {
                joystickRight.setOnAnalogueChangedListener(this.gazYawJoypadListener);
                joystickRight.setAbsolute(false);
            } else {
                joystickRight = JoystickFactory.createAcceleroJoystick(this.view.getRootView().getContext(), false, this.gazYawJoypadListener);
            }
        }
        onJoypadsReady(z, joystickLeft, joystickRight);
    }

    protected void onInitButtonsMappingLeftHanded() {
        if (this.buttonsMapping == null) {
            this.buttonsMapping = new SparseArray(6);
        } else {
            this.buttonsMapping.clear();
        }
    }

    protected void onInitButtonsMappingRightHanded() {
        if (this.buttonsMapping == null) {
            this.buttonsMapping = new SparseArray(6);
        } else {
            this.buttonsMapping.clear();
        }
    }

    protected void onInitOnScreenJoysticks(ControlMode controlMode, boolean z) {
        switch (controlMode) {
            case NORMAL_MODE:
                initVirtualJoysticks(JoystickType.ANALOGUE, JoystickType.ANALOGUE, z);
                return;
            case ACCELERO_MODE:
                initVirtualJoysticks(JoystickType.ACCELERO, JoystickType.ANALOGUE, z);
                return;
            case ACE_MODE:
                initVirtualJoysticks(JoystickType.NONE, JoystickType.COMBINED, z);
                return;
            default:
                return;
        }
    }

    protected void onJoypadsReady(boolean z, JoystickBase joystickBase, JoystickBase joystickBase2) {
        if (z) {
            this.view.setJoysticks(joystickBase2, joystickBase);
        } else {
            this.view.setJoysticks(joystickBase, joystickBase2);
        }
    }

    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (this.buttonsMapping != null) {
            Action action = (Action) this.buttonsMapping.get(i);
            if (action != null) {
                if (keyEvent.getAction() == 0) {
                    return processActionDown(action);
                }
                if (keyEvent.getAction() == 1) {
                    return processActionUp(action);
                }
            }
        }
        return false;
    }

    protected void onSettingsChanged() {
        onInitOnScreenJoysticks(this.settings.getControlMode(), this.settings.isLeftHanded());
        if (this.settings.isLeftHanded()) {
            onInitButtonsMappingLeftHanded();
        } else {
            onInitButtonsMappingRightHanded();
        }
        this.view.setInterfaceOpacity((float) this.settings.getInterfaceOpacity());
    }

    protected boolean processActionDown(Action action) {
        switch (action) {
            case UP:
                if (this.gazYawListener != null) {
                    this.gazYawListener.onGazYawActivated();
                    this.gazYawListener.onGazYawChanged(1.0f, 0.0f);
                    break;
                }
                break;
            case DOWN:
                if (this.gazYawListener != null) {
                    this.gazYawListener.onGazYawActivated();
                    this.gazYawListener.onGazYawChanged(GroundOverlayOptions.NO_DIMENSION, 0.0f);
                    break;
                }
                break;
            case TURN_LEFT:
                if (this.gazYawListener != null) {
                    this.gazYawListener.onGazYawActivated();
                    this.gazYawListener.onGazYawChanged(0.0f, GroundOverlayOptions.NO_DIMENSION);
                    break;
                }
                break;
            case TURN_RIGHT:
                if (this.gazYawListener != null) {
                    this.gazYawListener.onGazYawActivated();
                    this.gazYawListener.onGazYawChanged(0.0f, 1.0f);
                    break;
                }
                break;
            case ACCELERO:
                if (this.pitchRollListener != null) {
                    this.pitchRollListener.onRollPitchActivated();
                    break;
                }
                break;
            case FLIP:
                if (this.lastFlipClick - System.currentTimeMillis() < 500 && this.droneFlipListener != null) {
                    this.droneFlipListener.onFlip();
                    this.lastFlipClick = 0;
                }
                this.lastFlipClick = System.currentTimeMillis();
                break;
            default:
                Log.w(TAG, "Unknow action " + action.name());
                return false;
        }
        return true;
    }

    protected boolean processActionUp(Action action) {
        switch (action) {
            case UP:
            case DOWN:
            case TURN_LEFT:
            case TURN_RIGHT:
                if (this.gazYawListener != null) {
                    this.gazYawListener.onGazYawChanged(0.0f, 0.0f);
                    this.gazYawListener.onGazYawDeactivated();
                    break;
                }
                break;
            case TAKE_OFF:
                if (this.uiActionsListener != null) {
                    this.uiActionsListener.onTakeOffLandClicked();
                    break;
                }
                break;
            case ACCELERO:
                if (this.pitchRollListener != null) {
                    this.pitchRollListener.onRollPitchDeactivated();
                    break;
                }
                break;
            case CAMERA:
                if (this.uiActionsListener != null) {
                    this.uiActionsListener.onCameraSwitchClicked();
                    break;
                }
                break;
            case EMERGENCY:
                if (this.uiActionsListener != null) {
                    this.uiActionsListener.onEmergencyClicked();
                    break;
                }
                break;
            case SETTINGS:
                if (this.uiActionsListener != null) {
                    this.uiActionsListener.onSettingsClicked();
                    break;
                }
                break;
            case VIDEO_RECORD:
                if (this.uiActionsListener != null) {
                    this.uiActionsListener.onVideoRecordClicked();
                    break;
                }
                break;
            case PHOTO:
                if (this.uiActionsListener != null) {
                    this.uiActionsListener.onPhotoTakeClicked();
                    break;
                }
                break;
            default:
                Log.w(TAG, "Unknow action " + action.name());
                return false;
        }
        return true;
    }

    public void setOnFlipListener(OnDroneFlipListener onDroneFlipListener) {
        this.droneFlipListener = onDroneFlipListener;
    }

    public void setOnGazYawChangedListener(OnGazYawChangedListener onGazYawChangedListener) {
        this.gazYawListener = onGazYawChangedListener;
    }

    public void setOnRollPitchChangedListener(OnRollPitchChangedListener onRollPitchChangedListener) {
        this.pitchRollListener = onRollPitchChangedListener;
    }

    public void setOnUiActionsListener(OnUiActionsListener onUiActionsListener) {
        this.uiActionsListener = onUiActionsListener;
    }

    public void setSettings(ApplicationSettings applicationSettings) {
        this.settings = applicationSettings;
        onSettingsChanged();
    }
}
