const Input = {
    KEYCODE_MAP: {
        LEFT: 37,
        UP: 38,
        RIGHT: 39,
        DOWN: 40,
        P: 80,
    },

    KEY_MAP: {},

    KEY_STATES: {},

    init: function() {
        this.gamepad = null
        this.prevTimestamp = null

        this.configureKeys()
        document.addEventListener('keydown', function(e) {
            Input.changeKey(e.keyCode, 1)
        })
        document.addEventListener('keyup', function(e) {
            Input.changeKey(e.keyCode, 0)
        })
        this.startPolling()
    },

    configureKeys() {
        let count = 0

        for (let key in this.KEYCODE_MAP) {
            this.KEY_MAP[key] = this.KEYCODE_MAP[key]
            this.KEY_STATES[this.KEY_MAP[key]] = 0
            count++
        }
    },

    changeKey: function(key, value) {
        if (key == this.KEYCODE_MAP.P && value == 0) {
            Game.is_paused = !Game.is_paused
        } else {
            this.KEY_STATES[key] = value
        }
    },

    isPressed(key = '') {
        key = key.toUpperCase()
        if (this.KEY_STATES[this.KEY_MAP[key]] !== undefined) {
            return this.KEY_STATES[this.KEY_MAP[key]]
        }
    },

    startPolling() {
        requestAnimationFrame(this.startPolling.bind(this))

        this.gamepad = navigator.getGamepads()[0]

        if (!this.gamepad) return
        if (
            this.gamepad.timestamp &&
            this.gamepad.timestamp == Input.prevTimestamp
        ) {
            return
        }

        this.prevTimestamp = this.gamepad.timestamp

        this.updateKeys()
    },

    updateKeys() {
        Input.changeKey(this.KEYCODE_MAP.UP, 0)
        Input.changeKey(this.KEYCODE_MAP.DOWN, 0)
        Input.changeKey(this.KEYCODE_MAP.LEFT, 0)
        Input.changeKey(this.KEYCODE_MAP.RIGHT, 0)

        if (this.gamepad.axes[1] >= 0.5) {
            Input.changeKey(this.KEYCODE_MAP.DOWN, 1)
        }

        if (this.gamepad.axes[1] <= -0.5) {
            Input.changeKey(this.KEYCODE_MAP.UP, 1)
        }

        if (this.gamepad.axes[0] >= 0.5) {
            Input.changeKey(this.KEYCODE_MAP.RIGHT, 1)
        }

        if (this.gamepad.axes[0] <= -0.5) {
            Input.changeKey(this.KEYCODE_MAP.LEFT, 1)
        }
    },
}