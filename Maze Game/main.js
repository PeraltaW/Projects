window.onload = function() {
    canvas = document.getElementById('canvas')
    canvas.width = window.innerWidth * 0.9
    canvas.height = window.innerHeight * 0.9
    canvas.style.margin = 'auto'

    Game.init(canvas)
}

var Game = {
    canvas: null,
    ctx: null,
    ui: null,
    player: null,
    maze: null,
    paused: false,
    reqanimationreference: null,

    init: function(canvas) {
        this.canvas = canvas
        this.ctx = canvas.getContext('2d')
        this.ui = new UI()

        this.ui.showWelcomeScreen()
    },

    start: function(_columns) {
        var columns = _columns
        var column_width = Math.floor(this.canvas.width / columns)

        this.maze = new maze(columns, column_width)
        this.maze.generate()

        this.canvas.width = this.maze.getTotalWidth()
        this.canvas.height = this.maze.getTotalHeight()

        this.player = new player(0, 0)

        Input.init()

        this.reqanimationreference = window.requestAnimationFrame(Game.update)
    },
}

Game.update = function() {
    if (Game.paused) {
        Game.ui.showGameOverScreen()
    } else {
        this.reqanimationreference = window.requestAnimationFrame(Game.update)
        if (Input.isPressed('up')) {
            Game.player.move('up')
        }
        if (Input.isPressed('down')) {
            Game.player.move('down')
        }
        if (Input.isPressed('left')) {
            Game.player.move('left')
        }
        if (Input.isPressed('right')) {
            Game.player.move('right')
        }

        Game.render()
    }
}

Game.render = function() {
    this.ctx.clearRect(0, 0, canvas.width, canvas.height)
    this.maze.render()
    this.player.render()
}